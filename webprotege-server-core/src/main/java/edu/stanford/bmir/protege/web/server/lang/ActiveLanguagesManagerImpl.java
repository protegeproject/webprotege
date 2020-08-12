package edu.stanford.bmir.protege.web.server.lang;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.shortform.WellKnownLabellingIris.isWellKnownLabellingIri;
import static java.util.Comparator.comparing;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2018
 *
 * Builds and maintains a list of active languages that are used in a project.  The
 * list can be kept up to date in response to ontology changes.
 */
@ProjectSingleton
public class ActiveLanguagesManagerImpl implements ActiveLanguagesManager {

    private static final Logger logger = LoggerFactory.getLogger(ActiveLanguagesManagerImpl.class);

    private final ProjectId projectId;

    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    private final Multiset<DictionaryLanguage> activeLangs = HashMultiset.create();

    private ImmutableList<DictionaryLanguage> sortedLanguages = null;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public ActiveLanguagesManagerImpl(@Nonnull ProjectId projectId,
                                      @Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                                      @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectId = projectId;
        this.axiomsByEntityReferenceIndex = axiomsByEntityReferenceIndex;
        this.projectOntologiesIndex = projectOntologiesIndex;
    }

    private static boolean isLabellingAnnotation(OWLAnnotationAssertionAxiom ax) {
        return ax.getValue() instanceof OWLLiteral
                && isWellKnownLabellingIri(ax.getProperty().getIRI());
    }

    /**
     * Gets the languages used in the project, ranked in descending order in terms
     * of usage – the most commonly used languages appear first.  This ranking may change
     * as the ontologies in a project are edited.
     */
    @Override
    @Nonnull
    public synchronized ImmutableList<DictionaryLanguage> getLanguagesRankedByUsage() {
        buildIfNecessary();
        return sortedLanguages;
    }

    private void buildIfNecessary() {
        if (sortedLanguages == null) {
            rebuild();
        }
    }

    /**
     * Gets the languages used in the project, ranked in descending order in terms of usage.  The most commonly
     * user languages appear first.
     */
    @Override
    @Nonnull
    public synchronized ImmutableList<DictionaryLanguageUsage> getLanguageUsage() {
        ImmutableList<DictionaryLanguage> langs = getLanguagesRankedByUsage();
        return langs.stream()
                    .map(lang -> {
                        return lang.accept(new DictionaryLanguageVisitor<DictionaryLanguageUsage>() {
                            @Override
                            public DictionaryLanguageUsage visit(@Nonnull LocalNameDictionaryLanguage language) {
                                return DictionaryLanguageUsage.get(DictionaryLanguage.localName(),
                                                                   0);
                            }

                            @Override
                            public DictionaryLanguageUsage visit(@Nonnull OboIdDictionaryLanguage language) {
                                return DictionaryLanguageUsage.get(DictionaryLanguage.oboId(),
                                                                   0);
                            }

                            @Override
                            public DictionaryLanguageUsage visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
                                return DictionaryLanguageUsage.get(language, activeLangs.count(lang));
                            }

                            @Override
                            public DictionaryLanguageUsage visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
                                return DictionaryLanguageUsage.get(language, activeLangs.count(lang));
                            }

                            @Override
                            public DictionaryLanguageUsage visit(@Nonnull PrefixedNameDictionaryLanguage language) {
                                return DictionaryLanguageUsage.get(DictionaryLanguage.prefixedName(),
                                                                   0);
                            }
                        });
                    })
                    .collect(toImmutableList());
    }

    /**
     * Updates the active languages from the list of applied changes
     *
     * @param changes The changes.
     */
    @Override
    public synchronized void handleChanges(@Nonnull List<OntologyChange> changes) {
        if (changes.isEmpty()) {
            return;
        }
        buildIfNecessary();
        changes.stream()
               .filter(chg -> chg.isChangeFor(ANNOTATION_ASSERTION))
               .filter(chg -> isLabellingAnnotation((OWLAnnotationAssertionAxiom) chg.getAxiomOrThrow()))
               .forEach(chg -> {
                   var axiom = chg.getAxiomOrThrow();
                   if (chg.isAddAxiom()) {
                       addAxiom((OWLAnnotationAssertionAxiom) axiom);
                   }
                   else {
                       removeAxiom((OWLAnnotationAssertionAxiom) axiom);
                   }
               });
        rebuildSortedLanguages();
    }

    private void rebuild() {
        activeLangs.clear();
        Stopwatch stopwatch = Stopwatch.createStarted();
        projectOntologiesIndex.getOntologyIds().forEach(ontId -> {
            Stream.of(WellKnownLabellingIris.values())
                  .map(labellingIri -> new OWLAnnotationPropertyImpl(labellingIri.getIri()))
                  .flatMap(prop -> axiomsByEntityReferenceIndex.getReferencingAxioms(prop, ontId))
                  .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                  .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                  .filter(ax -> ax.getValue().isLiteral())
                  .forEach(this::addAxiom);
        });
        stopwatch.stop();
        logger.info("{} Extracted {} languages in {} ms", projectId, activeLangs.elementSet().size(), stopwatch.elapsed(MILLISECONDS));
        rebuildSortedLanguages();
        logSortedLanguages();
    }

    private void addAxiom(OWLAnnotationAssertionAxiom ax) {
        OWLLiteral literal = (OWLLiteral) ax.getValue();
        DictionaryLanguage lang = AnnotationAssertionDictionaryLanguage.get(ax.getProperty().getIRI(),
                                                                            literal.getLang());
        activeLangs.add(lang);
    }

    private void removeAxiom(OWLAnnotationAssertionAxiom ax) {
        OWLLiteral literal = (OWLLiteral) ax.getValue();
        DictionaryLanguage lang = AnnotationAssertionDictionaryLanguage.get(ax.getProperty().getIRI(),
                                                            literal.getLang());
        activeLangs.remove(lang);
    }

    private void rebuildSortedLanguages() {
        List<DictionaryLanguage> sortedLangs = new ArrayList<>(activeLangs.elementSet());
        Comparator<DictionaryLanguage> byActiveLangCount = comparing(activeLangs::count);
        Comparator<DictionaryLanguage> byActiveLangCountReversed = byActiveLangCount.reversed();
        Comparator<DictionaryLanguage> byLang = comparing(DictionaryLanguage::getLang);
        sortedLangs.sort(byActiveLangCountReversed.thenComparing(byLang));
        sortedLangs.add(PrefixedNameDictionaryLanguage.get());
        // TODO: Obo?
        sortedLangs.add(LocalNameDictionaryLanguage.get());
        sortedLanguages = ImmutableList.copyOf(sortedLangs);
    }

    private void logSortedLanguages() {
        sortedLanguages.forEach(language -> logger.info("{}     {} {}",
                                                        projectId,
                                                        language,
                                                        activeLangs.count(language)));
    }
}

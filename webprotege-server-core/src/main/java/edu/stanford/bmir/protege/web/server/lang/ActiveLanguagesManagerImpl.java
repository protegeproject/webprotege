package edu.stanford.bmir.protege.web.server.lang;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguageComparators;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.shortform.WellKnownLabellingIris.isWellKnownLabellingIri;
import static java.util.Comparator.comparing;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2018
 *
 * Builds and maintains a list of active languages that are used in a project.  The
 * list can be kept up to date in response to ontology changes.
 */
public class ActiveLanguagesManagerImpl implements ActiveLanguagesManager {

    private static final Logger logger = LoggerFactory.getLogger(ActiveLanguagesManagerImpl.class);

    private final ProjectId projectId;

    private final AnnotationAssertionAxiomsIndex annotationAssertions;

    private final Multiset<DictionaryLanguage> activeLangs = HashMultiset.create();

    private ImmutableList<DictionaryLanguage> sortedLanguages = null;

    @Inject
    public ActiveLanguagesManagerImpl(ProjectId projectId, AnnotationAssertionAxiomsIndex annotationAssertions) {
        this.projectId = projectId;
        this.annotationAssertions = annotationAssertions;
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
        if (sortedLanguages == null) {
            rebuild();
        }
        return sortedLanguages;
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
                        if (lang.isAnnotationBased()) {
                            DictionaryLanguageData langData = DictionaryLanguageData.get(lang.getAnnotationPropertyIri(),
                                                                                         lang.getLang());
                            return DictionaryLanguageUsage.get(langData, activeLangs.count(lang));
                        }
                        else {
                            return DictionaryLanguageUsage.get(DictionaryLanguageData.localName(), 0);
                        }
                    })
                    .collect(toImmutableList());
    }

    /**
     * Updates the active languages from the list of applied changes
     *
     * @param changes The changes.
     */
    @Override
    public synchronized void handleChanges(@Nonnull List<OWLOntologyChange> changes) {
        if (changes.isEmpty()) {
            return;
        }
        changes.stream()
               .filter(OWLOntologyChange::isAxiomChange)
               .filter(chg -> chg.getAxiom() instanceof OWLAnnotationAssertionAxiom)
               .filter(chg -> isLabellingAnnotation((OWLAnnotationAssertionAxiom) chg.getAxiom()))
               .forEach(chg -> {
                   if (chg.isAddAxiom()) {
                       addAxiom((OWLAnnotationAssertionAxiom) chg.getAxiom());
                   }
                   else {
                       removeAxiom((OWLAnnotationAssertionAxiom) chg.getAxiom());
                   }
               });
        rebuildSortedLanguages();
    }

    private void rebuild() {
        activeLangs.clear();
        Stopwatch stopwatch = Stopwatch.createStarted();
        annotationAssertions.getAnnotationAssertionAxioms()
                    .filter(ActiveLanguagesManagerImpl::isLabellingAnnotation)
                    .forEach(this::addAxiom);
        stopwatch.stop();
        logger.info("{} Extracted {} languages in {} ms", projectId, activeLangs.elementSet().size(), stopwatch.elapsed(MILLISECONDS));
        rebuildSortedLanguages();
        logSortedLanguages();
    }

    private void addAxiom(OWLAnnotationAssertionAxiom ax) {
        OWLLiteral literal = (OWLLiteral) ax.getValue();
        DictionaryLanguage lang = DictionaryLanguage.create(ax.getProperty().getIRI(),
                                                            literal.getLang());
        activeLangs.add(lang);
    }

    private void removeAxiom(OWLAnnotationAssertionAxiom ax) {
        OWLLiteral literal = (OWLLiteral) ax.getValue();
        DictionaryLanguage lang = DictionaryLanguage.create(ax.getProperty().getIRI(),
                                                            literal.getLang());
        activeLangs.remove(lang);
    }

    private void rebuildSortedLanguages() {
        List<DictionaryLanguage> sortedLangs = new ArrayList<>(activeLangs.elementSet());
        Comparator<DictionaryLanguage> byActiveLangCount = comparing(activeLangs::count);
        Comparator<DictionaryLanguage> byActiveLangCountReversed = byActiveLangCount.reversed();
        Comparator<DictionaryLanguage> byLang = DictionaryLanguageComparators.byLang();
        sortedLangs.sort(byActiveLangCountReversed.thenComparing(byLang));
        sortedLangs.add(DictionaryLanguage.localName());
        sortedLanguages = ImmutableList.copyOf(sortedLangs);
    }

    private void logSortedLanguages() {
        sortedLanguages.forEach(language -> logger.info("{}     {} {}",
                                                        projectId,
                                                        language,
                                                        activeLangs.count(language)));
    }
}

package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
public class ActiveLanguagesManager {

    private static final Logger logger = LoggerFactory.getLogger(ActiveLanguagesManager.class);

    private final ProjectId projectId;

    private final OWLOntology rootOntology;

    private final Multiset<DictionaryLanguage> activeLangs = HashMultiset.create();

    private ImmutableList<DictionaryLanguage> sortedLanguages = null;

    @Inject
    public ActiveLanguagesManager(ProjectId projectId, OWLOntology rootOntology) {
        this.projectId = projectId;
        this.rootOntology = rootOntology;
    }

    /**
     * Gets the languages used in the project, ranked in descending order in terms
     * of usage – the most commonly used languages appear first.  This ranking may change
     * as the ontologies in a project are edited.
     */
    @Nonnull
    public synchronized ImmutableList<DictionaryLanguage> getLanguagesRankedByUsage() {
        if (sortedLanguages == null) {
            rebuild();
        }
        return sortedLanguages;
    }

    /**
     * Updates the active languages from the list of applied changes
     * @param changes The changes.
     */
    public synchronized void handleChanges(@Nonnull List<OWLOntologyChange> changes) {
        if(changes.isEmpty()) {
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
        rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getAxioms(ANNOTATION_ASSERTION).stream())
                    .filter(ActiveLanguagesManager::isLabellingAnnotation)
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


    private static boolean isLabellingAnnotation(OWLAnnotationAssertionAxiom ax) {
        return ax.getValue() instanceof OWLLiteral && (ax.getProperty().isLabel() || ax.getProperty().getIRI().equals(SKOSVocabulary.PREFLABEL.getIRI()));
    }
}

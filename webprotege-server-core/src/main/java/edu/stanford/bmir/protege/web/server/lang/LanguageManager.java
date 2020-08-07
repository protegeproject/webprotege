package edu.stanford.bmir.protege.web.server.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionPathDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2018
 */
@ProjectSingleton
public class LanguageManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActiveLanguagesManager activeLanguagesManager;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Inject
    public LanguageManager(@Nonnull ProjectId projectId,
                           @Nonnull ActiveLanguagesManager extractor,
                           @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        this.projectId = projectId;
        this.activeLanguagesManager = checkNotNull(extractor);
        this.projectDetailsRepository = projectDetailsRepository;
    }

    public synchronized List<DictionaryLanguage> getLanguages() {
        ImmutableList<DictionaryLanguage> defaultDisplayLanguages = projectDetailsRepository.getDisplayNameLanguages(projectId);
        if (defaultDisplayLanguages.isEmpty()) {
            return activeLanguagesManager.getLanguagesRankedByUsage();
        }
        else {
            var x = ImmutableList.<DictionaryLanguage>builder();
            x.addAll(defaultDisplayLanguages);
            x.add(AnnotationAssertionPathDictionaryLanguage.get(
                    ImmutableList.of(IRI.create("https://data.elsevier.com/health/core/schema/extExact"),
                                     OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                    "en"
            ));
            return x.build();
        }
    }

    public synchronized List<DictionaryLanguage> getActiveLanguages() {
        return activeLanguagesManager.getLanguagesRankedByUsage();
    }
}

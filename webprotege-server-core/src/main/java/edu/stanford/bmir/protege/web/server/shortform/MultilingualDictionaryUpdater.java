package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-01
 */
@ProjectSingleton
public interface MultilingualDictionaryUpdater {

    /**
     * Updates the dictionary entries for the specified entities.
     * @param entities The entities that will be updated.
     * @param languages The languages that should be updated.
     */
    void update(@Nonnull Collection<OWLEntity> entities,
                @Nonnull List<DictionaryLanguage> languages);
}

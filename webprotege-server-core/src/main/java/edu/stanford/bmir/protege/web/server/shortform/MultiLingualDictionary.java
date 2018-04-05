package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
@ProjectSingleton
public interface MultiLingualDictionary {

    @Nonnull
    Collection<DictionaryLanguage> getLanguages();

//    /**
//     * Gets the short form for an entity.
//     *
//     * @param entity           The entity.
//     * @param language         The preferred {@link DictionaryLanguage}. A short form for this language will be
//     *                         returned, if it exists.  If there is no short form for this language
//     *                         then the specified default short form will be returned.
//     * @param defaultShortForm The default short form that is returned if there is no short form for the
//     *                         specified language.
//     * @return The short form.
//     */
//    @Nullable
//    String getShortForm(@Nonnull OWLEntity entity,
//                        @Nonnull DictionaryLanguage language,
//                        @Nullable String defaultShortForm);

    @Nonnull
    String getShortForm(@Nonnull OWLEntity entity,
                        @Nonnull List<DictionaryLanguage> languages,
                        @Nonnull String defaultShortForm);

    void handleChanges(@Nonnull List<? extends OWLOntologyChange> changes);
}

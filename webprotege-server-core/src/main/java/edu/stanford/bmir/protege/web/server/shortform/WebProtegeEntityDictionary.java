package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
@ProjectSingleton
public interface WebProtegeEntityDictionary {

    void setDefaultLang(@Nonnull String lang);

    @Nonnull
    String getDefaultLang();

    Collection<String> getLangs();

    @Nonnull
    Collection<OWLEntity> getEntities(@Nonnull String shortForm);

    /**
     * Gets the short form in a given language
     * @param entity The entity.
     * @param lang The language.
     * @return The short form.
     */
    @Nonnull
    String getShortFormForLangOrEmptyLang(@Nonnull OWLEntity entity, @Nonnull String lang);

    /**
     * Gets the short form in the default language or in the empty language
     * @param entity The entity.
     * @return
     */
    @Nonnull
    String getShortFormForDefaultOrEmptyLang(@Nonnull OWLEntity entity);

    @Nonnull
    Collection<String> getShortForms(@Nonnull String lang);

    void handleChanges(List<? extends OWLOntologyChange> changes);
}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
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
public interface Dictionary {

    @Nonnull
    Collection<String> getLangs();


    @Nonnull
    Collection<OWLEntity> getEntities(@Nonnull IRI annotationPropertyIri,
                                      @Nonnull String preferredLang,
                                      @Nonnull String shortForm);

    /**
     * Gets the short form for an entity.
     *
     * @param entity                The entity.
     * @param annotationPropertyIri The preferred annotation property IRI that provides the short form.
     * @param lang                  The language tag.  A short form for this language tag will be
     *                              returned, if it exists.  If there is no short form for this language tag
     *                              then the specified default short form will be returned.
     * @param defaultShortForm      The default short form that is returned if there is no short form for the
     *                              specified language tag.
     * @return The short form.
     */
    @Nonnull
    String getShortForm(@Nonnull OWLEntity entity,
                        @Nonnull IRI annotationPropertyIri,
                        @Nonnull String lang,
                        @Nonnull String defaultShortForm);

    void handleChanges(@Nonnull List<? extends OWLOntologyChange> changes);
}

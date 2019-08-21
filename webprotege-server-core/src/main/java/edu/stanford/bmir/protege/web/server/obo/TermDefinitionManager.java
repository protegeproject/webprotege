package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.shared.obo.OBOTermDefinition;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
public interface TermDefinitionManager {

    /**
     * Gets the OBO Term Definition for an {@link OWLEntity}.
     * @param term The term definition
     */
    @Nonnull
    OBOTermDefinition getTermDefinition(@Nonnull OWLEntity term);

    /**
     * Sets the OBO Term Definition for an entity
     * @param userId The user setting the definition
     * @param term The entity that represents the term
     * @param definition The definition
     */
    void setTermDefinition(@Nonnull UserId userId,
                           @Nonnull OWLEntity term,
                           @Nonnull OBOTermDefinition definition);
}

package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public interface FreshEntitiesHandler {

    /**
     * Gets the policy supported by this handler.
     * @return The {@link FreshEntitiesPolicy}.  Not {@code null}.
     */
    FreshEntitiesPolicy getFreshEntitiesPolicy();

    /**
     * Gets the error message for when an entity is a fresh entity.
     * @param browserText The browser text being parsed.
     * @return An error message.  Not {@code null}.
     */
    String getErrorMessage(String browserText);

    /**
     * Gets a fresh entity of the given type with the specified browser text.
     * @param browserText The browser text. Not {@code null}.
     * @param type The type.  Not {@code null}.
     * @param <E> The fresh entity type.
     * @return The fresh entity.  Not {@code null}.
     */
    <E extends OWLEntity> E getFreshEntity(String browserText, EntityType<E> type);

    <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText, EntityType<E> entityType);

    <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText);

    boolean isRegisteredFreshEntity(String browserText);
}

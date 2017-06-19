package edu.stanford.bmir.protege.web.client.primitive;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class NullFreshEntitiesHandler implements FreshEntitiesHandler {

    @Inject
    public NullFreshEntitiesHandler() {
    }

    @Override
    public FreshEntitiesPolicy getFreshEntitiesPolicy() {
        return FreshEntitiesPolicy.NOT_ALLOWED;
    }

    @Override
    public <E extends OWLEntity> E getFreshEntity(String browserText, EntityType<E> type) {
        throw new UnsupportedOperationException("Fresh entities are not supported");
    }

    @Override
    public boolean isRegisteredFreshEntity(String browserText) {
        return false;
    }

    @Override
    public <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText, EntityType<E> entityType) {
        return Optional.empty();
    }

    @Override
    public <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText) {
        return Optional.empty();
    }

    /**
     * Gets the error message for when an entity is a fresh entity.
     * @param browserText The browser text being parsed.
     * @return An error message.  Not {@code null}.
     */
    @Override
    public String getErrorMessage(String browserText) {
        return browserText + " does not exist";
    }
}

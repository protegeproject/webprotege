package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public class PrimitiveDataParsingContext implements FreshEntitiesHandler {

    private ProjectId projectId;

    private Set<PrimitiveType> allowedTypes = new HashSet<PrimitiveType>();

    private FreshEntitiesHandler freshEntitiesHandler;

    public PrimitiveDataParsingContext(ProjectId projectId, Set<PrimitiveType> allowedTypes, FreshEntitiesHandler freshEntitiesHandler) {
        this.projectId = projectId;
        this.allowedTypes = new HashSet<PrimitiveType>(allowedTypes);
        this.freshEntitiesHandler = freshEntitiesHandler;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the error message for when an entity is a fresh entity.
     * @param browserText The browser text being parsed.
     * @return An error message.  Not {@code null}.
     */
    @Override
    public String getErrorMessage(String browserText) {
        return freshEntitiesHandler.getErrorMessage(browserText);
    }

    public boolean isLiteralAllowed() {
        return allowedTypes.contains(PrimitiveType.LITERAL);
    }

    public boolean isIRIAllowed() {
        return allowedTypes.contains(PrimitiveType.IRI);
    }

    public boolean isEntitiesAllowed() {
        for (PrimitiveType primitiveType : allowedTypes) {
            if (primitiveType.isEntityType()) {
                return true;
            }
        }
        return false;
    }

    public boolean isTypeAllowed(PrimitiveType type) {
        return allowedTypes.contains(type);
    }




    /**
     * Gets the policy supported by this handler.
     * @return The {@link FreshEntitiesPolicy}.  Not {@code null}.
     */
    @Override
    public FreshEntitiesPolicy getFreshEntitiesPolicy() {
        return freshEntitiesHandler.getFreshEntitiesPolicy();
    }

    /**
     * Gets a fresh entity of the given type with the specified browser text.
     * @param browserText The browser text. Not {@code null}.
     * @param type The type.  Not {@code null}.
     * @param <E> The fresh entity type.
     * @return The fresh entity.  Not {@code null}.
     */
    @Override
    public <E extends OWLEntity> E getFreshEntity(String browserText, EntityType<E> type) {
        return freshEntitiesHandler.getFreshEntity(browserText, type);
    }

    @Override
    public <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText, EntityType<E> entityType) {
        return freshEntitiesHandler.getRegisteredFreshEntity(browserText, entityType);
    }

    @Override
    public <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText) {
        return freshEntitiesHandler.getRegisteredFreshEntity(browserText);
    }

    @Override
    public boolean isRegisteredFreshEntity(String browserText) {
        return freshEntitiesHandler.isRegisteredFreshEntity(browserText);
    }

    public Set<EntityType<?>> getAllowedEntityTypes() {
        Set<EntityType<?>> types = new HashSet<EntityType<?>>();
        for (PrimitiveType primitiveType : allowedTypes) {
            EntityType<?> entityType = primitiveType.getEntityType();
            if (entityType != null) {
                types.add(entityType);
            }
        }
        return types;
    }
}

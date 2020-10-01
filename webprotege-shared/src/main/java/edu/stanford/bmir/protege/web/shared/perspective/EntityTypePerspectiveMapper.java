package edu.stanford.bmir.protege.web.shared.perspective;

import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 *
 * Maps Entity Types to Perspective Ids.
 */
public class EntityTypePerspectiveMapper {

    public static final PerspectiveId CLASSES_PERSPECTIVE = PerspectiveId.get("69df8fa8-4f84-499e-9341-28eb5085c40b");

    public static final PerspectiveId PROPERTIES_PERSPECTIVE = PerspectiveId.get("be7befeb-e42e-408d-a5bc-b040d70d3d77");

    public static final PerspectiveId INDIVIDUALS_PERSPECTIVE = PerspectiveId.get("533ca7cb-2af7-4331-b9b4-d0f8603acac2");

    @Inject
    public EntityTypePerspectiveMapper() {
    }

    /**
     * Gets the default {@link PerspectiveId}, which is used for the empty selection.
     * @return The default {@link PerspectiveId}.
     */
    @Nonnull
    public PerspectiveId getDefaultPerspectiveId() {
        return CLASSES_PERSPECTIVE;
    }

    /**
     * Gets the default {@link PerspectiveId} for a given {@link EntityType}.
     * @param entityType The {@link EntityType}.
     * @return The default {@link PerspectiveId} for the specified entity type.
     */
    @Nonnull
    public PerspectiveId getPerspectiveId(@Nonnull EntityType<?> entityType) {
        if(entityType == CLASS) {
            return CLASSES_PERSPECTIVE;
        }
        else if(entityType == OBJECT_PROPERTY) {
            return PROPERTIES_PERSPECTIVE;
        }
        else if(entityType == DATA_PROPERTY) {
            return PROPERTIES_PERSPECTIVE;
        }
        else if(entityType == ANNOTATION_PROPERTY) {
            return PROPERTIES_PERSPECTIVE;
        }
        else if(entityType == NAMED_INDIVIDUAL) {
            return INDIVIDUALS_PERSPECTIVE;
        }
        else {
            return CLASSES_PERSPECTIVE;
        }
    }
}

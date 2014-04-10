package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public enum PrimitiveType {

    CLASS(EntityType.CLASS),

    NAMED_INDIVIDUAL(EntityType.NAMED_INDIVIDUAL),


    OBJECT_PROPERTY(EntityType.OBJECT_PROPERTY),

    DATA_PROPERTY(EntityType.DATA_PROPERTY),

    ANNOTATION_PROPERTY(EntityType.ANNOTATION_PROPERTY),

    DATA_TYPE(EntityType.DATATYPE),

    LITERAL(null),

    IRI(null);


    private final EntityType<?> entityType;

    private PrimitiveType(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * Gets the {@link EntityType} corresponding to this {@link PrimitiveType} (if there is a corresponding one).
     * @return The corresponding {@link EntityType} or {@code null} if this primitive type is a #LITERAL or #IRI
     * and therefore does not correspond to an {@link EntityType}.
     */
    public EntityType<?> getEntityType() {
        return entityType;
    }

    public boolean isEntityType() {
        return entityType != null;
    }

    public static PrimitiveType get(EntityType<?> entityType) {
        if(entityType == EntityType.CLASS) {
            return CLASS;
        }
        else if(entityType == EntityType.OBJECT_PROPERTY) {
            return OBJECT_PROPERTY;
        }
        else if(entityType == EntityType.DATA_PROPERTY) {
            return DATA_PROPERTY;
        }
        else if(entityType == EntityType.ANNOTATION_PROPERTY) {
            return ANNOTATION_PROPERTY;
        }
        else if(entityType == EntityType.NAMED_INDIVIDUAL) {
            return NAMED_INDIVIDUAL;
        }
        else if(entityType == EntityType.DATATYPE) {
            return DATA_TYPE;
        }
        else {
            throw new RuntimeException("Unrecognized entity type " + entityType);
        }
    }

}

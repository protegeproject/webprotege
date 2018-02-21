package edu.stanford.bmir.protege.web.shared.csv;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/05/2013
 */
public enum ColumnType {


    CLASS("Class", PrimitiveType.CLASS, EntityType.OBJECT_PROPERTY, EntityType.ANNOTATION_PROPERTY),

    NAMED_INDIVIDUAL("Individual", PrimitiveType.NAMED_INDIVIDUAL, EntityType.OBJECT_PROPERTY, EntityType.ANNOTATION_PROPERTY),

    STRING("String", PrimitiveType.LITERAL, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY),

    INTEGER("Integer", PrimitiveType.LITERAL, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY),

    DOUBLE("Double", PrimitiveType.LITERAL, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY),

    BOOLEAN("Boolean", PrimitiveType.LITERAL, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY);





    /**
     * Gets a column type from its unique display name.
     * @param displayName The display name.  Not {@code null}.
     * @return The column type that has the specified display name.  Not {@code null}.
     * @throws NullPointerException if displayName is null;
     * @throws IllegalArgumentException if there is no column with the specified display name.
     */
    public static ColumnType getColumnTypeFromDisplayName(String displayName) {
        checkNotNull(displayName);
        for(ColumnType columnType : values()) {
            if(columnType.getDisplayName().equals(displayName)) {
                return columnType;
            }
        }
        throw new IllegalArgumentException("There is no ColumnType with the specified display name: " + displayName);
    }

    private String displayName;

    private PrimitiveType primitiveType;

    private List<EntityType<?>> propertyTypes;

    /**
     * For serialization only
     */
    ColumnType() {
    }

    ColumnType(String displayName, PrimitiveType primitiveType, EntityType<?>... propertyTypes) {
        this.displayName = displayName;
        this.primitiveType = primitiveType;
        this.propertyTypes = Arrays.asList(propertyTypes);
    }


    public boolean isPropertyType(EntityType<?> entityType) {
        return propertyTypes.contains(entityType);
    }


    public String getDisplayName() {
        return displayName;
    }

    public PrimitiveType getPrimitiveType() {
        return primitiveType;
    }

    public List<EntityType<?>> getPropertyTypes() {
        return new ArrayList<EntityType<?>>(propertyTypes);
    }
}

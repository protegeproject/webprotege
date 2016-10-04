package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public enum EntityTypeGroup {

    CLASS("Class", EntityType.CLASS),

    PROPERTY("Property", EntityType.OBJECT_PROPERTY, EntityType.DATA_PROPERTY, EntityType.ANNOTATION_PROPERTY),

    INDIVIDUAL("Individual", EntityType.NAMED_INDIVIDUAL),

    DATATYPE("Datatype", EntityType.DATATYPE);

    private List<EntityType<?>> entityTypes;

    private String displayName;

    private EntityTypeGroup(String displayName, EntityType<?> ... entityTypes) {
        this.entityTypes = Collections.unmodifiableList(new ArrayList<EntityType<?>>(Arrays.asList(entityTypes)));
        this.displayName = displayName;
    }

    public List<EntityType<?>> getEntityTypes() {
        return entityTypes;
    }

    public String getDisplayName() {
        return displayName;
    }
}

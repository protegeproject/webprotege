package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;

import java.io.Serializable;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermCrossProduct implements Serializable {

    private OWLClassData genus;

    private OBOTermRelationships relationships;

    private OBOTermCrossProduct() {
    
    }
    
    public static OBOTermCrossProduct emptyOBOTermCrossProduct() {
        return new OBOTermCrossProduct(null, new OBOTermRelationships(Collections.<OBORelationship>emptySet()));
    }

    public OBOTermCrossProduct(OWLClassData genus, OBOTermRelationships relationships) {
        this.genus = genus;
        this.relationships = relationships;
    }

    public OBOTermCrossProduct(OWLClassData genus) {
        this(genus, new OBOTermRelationships(Collections.<OBORelationship>emptySet()));
    }

    public OWLClassData getGenus() {
        return genus;
    }

    public OBOTermRelationships getRelationships() {
        return relationships;
    }
    
    public boolean isEmpty() {
        return relationships.isEmpty();
    }
}

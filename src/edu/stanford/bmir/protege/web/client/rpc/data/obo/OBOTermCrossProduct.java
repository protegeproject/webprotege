package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualNamedClass;

import java.io.Serializable;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermCrossProduct implements Serializable {

    private VisualNamedClass genus;

    private OBOTermRelationships relationships;

    private OBOTermCrossProduct() {
    
    }
    
    public static OBOTermCrossProduct emptyOBOTermCrossProduct() {
        return new OBOTermCrossProduct(null, new OBOTermRelationships(Collections.<OBORelationship>emptySet()));
    }

    public OBOTermCrossProduct(VisualNamedClass genus, OBOTermRelationships relationships) {
        this.genus = genus;
        this.relationships = relationships;
    }

    public OBOTermCrossProduct(VisualNamedClass genus) {
        this(genus, new OBOTermRelationships(Collections.<OBORelationship>emptySet()));
    }

    public VisualNamedClass getGenus() {
        return genus;
    }

    public OBOTermRelationships getRelationships() {
        return relationships;
    }
    
    public boolean isEmpty() {
        return relationships.isEmpty();
    }
}

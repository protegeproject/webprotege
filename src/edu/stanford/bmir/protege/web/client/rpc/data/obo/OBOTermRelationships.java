package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermRelationships implements Serializable {

    private Set<OBORelationship> relationships;

    private OBOTermRelationships() {
    }

    public OBOTermRelationships(Set<OBORelationship> relationships) {
        List<OBORelationship> relationshipList = new ArrayList<OBORelationship>(relationships);
        Collections.sort(relationshipList);
        this.relationships = new LinkedHashSet<OBORelationship>(relationshipList);
    }

    public Set<OBORelationship> getRelationships() {
        return new HashSet<OBORelationship>(relationships);
    }

    public boolean isEmpty() {
        return relationships.isEmpty();
    }

    @Override
    public String toString() {
        return "OBOTermRelationships(" + relationships.size() + ":  " + relationships + ")";
    }
}

package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBORelationship implements Serializable, Comparable<OBORelationship> {


    private OWLObjectPropertyData relation;
    
    private OWLClassData value;

    private OBORelationship() {
    }

    public OBORelationship(OWLObjectPropertyData relation, OWLClassData value) {
        this.relation = relation;
        this.value = value;
    }

    public OWLObjectPropertyData getRelation() {
        return relation;
    }

    public OWLClassData getValue() {
        return value;
    }


    public int compareTo(OBORelationship o) {
        int diff = relation.compareTo(o.getRelation());
        if(diff != 0) {
            return diff;
        }
        return value.compareTo(o.getValue());
    }
}

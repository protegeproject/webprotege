package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

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
        this.relation = checkNotNull(relation);
        this.value = checkNotNull(value);
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


    @Override
    public String toString() {
        return Objects.toStringHelper("OBORelationship")
                .addValue(relation)
                .addValue(value)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(relation, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OBORelationship)) {
            return false;
        }
        OBORelationship other = (OBORelationship) obj;
        return this.relation.equals(other.relation) && this.value.equals(other.value);
    }
}

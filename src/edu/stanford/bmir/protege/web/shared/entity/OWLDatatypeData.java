package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class OWLDatatypeData extends OWLEntityData {

    public OWLDatatypeData(OWLDatatype entity, String browserText) {
        super(entity, browserText);
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.DATA_TYPE;
    }

    @Override
    public OWLDatatype getEntity() {
        return (OWLDatatype) super.getEntity();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getEntity());
    }

    @Override
    public int hashCode() {
        return "OWLDatatypeData".hashCode() + getEntity().hashCode() + getBrowserText().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLDatatypeData)) {
            return false;
        }
        OWLDatatypeData other = (OWLDatatypeData) obj;
        return this.getEntity().equals(other.getEntity()) && this.getBrowserText().equals(other.getBrowserText());
    }
}

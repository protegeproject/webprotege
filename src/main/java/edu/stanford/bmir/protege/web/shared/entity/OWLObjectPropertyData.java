package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public final class OWLObjectPropertyData extends OWLPropertyData {

    public OWLObjectPropertyData(OWLObjectProperty entity, String browserText) {
        super(entity, browserText);
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.OBJECT_PROPERTY;
    }

    @Override
    public OWLObjectProperty getEntity() {
        return (OWLObjectProperty) super.getEntity();
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
        return "OWLObjectPropertyData".hashCode() + getEntity().hashCode() + getBrowserText().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLObjectPropertyData)) {
            return false;
        }
        OWLObjectPropertyData other = (OWLObjectPropertyData) obj;
        return this.getEntity().equals(other.getEntity()) && this.getBrowserText().equals(other.getBrowserText());
    }
}

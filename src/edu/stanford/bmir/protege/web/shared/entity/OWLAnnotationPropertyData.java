package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public final class OWLAnnotationPropertyData extends OWLPropertyData {

    public OWLAnnotationPropertyData(OWLAnnotationProperty entity, String browserText) {
        super(entity, browserText);
    }

    @Override
    public OWLAnnotationProperty getEntity() {
        return (OWLAnnotationProperty) super.getEntity();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.ANNOTATION_PROPERTY;
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
        return "OWLAnnotationPropertyData".hashCode() + this.getEntity().hashCode() + this.getBrowserText().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLAnnotationPropertyData)) {
            return false;
        }
        OWLAnnotationPropertyData other = (OWLAnnotationPropertyData) obj;
        return this.getEntity().equals(other.getEntity()) && this.getBrowserText().equals(other.getBrowserText());
    }
}

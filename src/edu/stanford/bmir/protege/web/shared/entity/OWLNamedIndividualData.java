package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public final class OWLNamedIndividualData extends OWLEntityData {

    public OWLNamedIndividualData(OWLNamedIndividual entity, String browserText) {
        super(entity, browserText);
    }

    @Override
    public OWLNamedIndividual getEntity() {
        return (OWLNamedIndividual) super.getEntity();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.NAMED_INDIVIDUAL;
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
        return "OWLNamedIndividualData".hashCode() + getEntity().hashCode() + getBrowserText().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLNamedIndividualData)) {
            return false;
        }
        OWLNamedIndividualData other = (OWLNamedIndividualData) obj;
        return this.getEntity().equals(other.getEntity()) && this.getBrowserText().equals(other.getBrowserText());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OWLNamedIndividualData");
        sb.append("(");
        sb.append("BrowserText(");
        sb.append(getBrowserText());
        sb.append(") NamedIndividual(");
        sb.append(getEntity());
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }
}

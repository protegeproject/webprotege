package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public class OWLClassData extends OWLEntityData {

    public OWLClassData(OWLClass entity, String browserText) {
        super(entity, browserText);
    }

    @Override
    public OWLClass getEntity() {
        return (OWLClass) super.getEntity();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.CLASS;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClassData(");
        sb.append(getBrowserText());
        sb.append(" ");
        sb.append(getObject());
        sb.append(")");
        return sb.toString();
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
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "OWLClassData".hashCode() + getEntity().hashCode() + getBrowserText().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLClassData)) {
            return false;
        }
        OWLClassData other = (OWLClassData) obj;
        return this.getEntity().equals(other.getEntity()) && this.getBrowserText().equals(other.getBrowserText());
    }
}

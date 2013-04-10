package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasLexicalForm;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public final class OWLLiteralData extends OWLPrimitiveData implements HasLexicalForm {

    public OWLLiteralData(OWLLiteral object) {
        super(object);
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.LITERAL;
    }

    @Override
    public OWLLiteral getObject() {
        return (OWLLiteral) super.getObject();
    }

    public OWLLiteral getLiteral() {
        return getObject();
    }

    @Override
    public String getBrowserText() {
        OWLLiteral literal = getLiteral();
        return literal.getLiteral();
    }

    @Override
    public String getUnquotedBrowserText() {
        return getBrowserText();
    }

    @Override
    public String getLexicalForm() {
        return getLiteral().getLiteral();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OWLLiteralData");
        sb.append("(");
        sb.append(getLiteral());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return defaultValue;
    }

    @Override
    public int hashCode() {
        return "OWLLiteralData".hashCode() + getLiteral().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OWLLiteralData)) {
            return false;
        }
        OWLLiteralData other = (OWLLiteralData) obj;
        return this.getLiteral().equals(other.getLiteral());
    }


}

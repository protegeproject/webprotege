package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2012
 */
public class IRIData extends OWLPrimitiveData {

    public IRIData(IRI object) {
        super(object);
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.IRI;
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
    public IRI getObject() {
        return (IRI) super.getObject();
    }

    @Override
    public String getBrowserText() {
        return getObject().toString();
    }

    public boolean isHTTPLink() {
        return "http".equalsIgnoreCase(getObject().getScheme());
    }

    @Override
    public String getUnquotedBrowserText() {
        return getObject().toString();
    }
}

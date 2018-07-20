package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/12/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class IRIData implements OWLPrimitiveData {

    public static IRIData get(@Nonnull IRI iri) {
        return new AutoValue_IRIData(iri);
    }

    @Nonnull
    @Override
    public abstract IRI getObject();

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
    public String getBrowserText() {
        return getObject().toString();
    }

    public boolean isHTTPLink() {
        return "http".equalsIgnoreCase(getObject().getScheme())
                || "https".equalsIgnoreCase(getObject().getScheme());
    }

    @Override
    public String getUnquotedBrowserText() {
        return getObject().toString();
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getObject());
    }
}

package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue
public abstract class OWLAnnotationPropertyData extends OWLPropertyData {

    public static OWLAnnotationPropertyData get(@Nonnull OWLAnnotationProperty property,
                                                @Nonnull String browserText) {
        return new AutoValue_OWLAnnotationPropertyData(browserText, property);
    }

    @Nonnull
    @Override
    public abstract OWLAnnotationProperty getObject();

    @Override
    public OWLAnnotationProperty getEntity() {
        return getObject();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getObject());
    }

    @Override
    public boolean isOWLAnnotationProperty() {
        return true;
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.ANNOTATION_PROPERTY;
    }

    @Override
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }
}

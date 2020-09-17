package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class OWLAnnotationPropertyData extends OWLPropertyData {

    public static final int BEFORE = -1;

    @Nonnull
    public static OWLAnnotationPropertyData get(@Nonnull OWLAnnotationProperty property,
                                                @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return new AutoValue_OWLAnnotationPropertyData(shortForms, false, property);
    }

    @Nonnull
    public static OWLAnnotationPropertyData get(@Nonnull OWLAnnotationProperty property,
                                                @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                                boolean deprecated) {
        return new AutoValue_OWLAnnotationPropertyData(shortForms, deprecated, property);
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

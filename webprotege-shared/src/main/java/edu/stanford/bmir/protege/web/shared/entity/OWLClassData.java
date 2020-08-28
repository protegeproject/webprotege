package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
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
@GwtCompatible(serializable = true)
public abstract class OWLClassData extends OWLEntityData {



    public static OWLClassData get(@Nonnull OWLClass cls,
                                   @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return new AutoValue_OWLClassData(shortForms, false, cls);
    }

    public static OWLClassData get(@Nonnull OWLClass cls,
                                   @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                   boolean deprecated) {
        return new AutoValue_OWLClassData(shortForms, deprecated, cls);
    }

    @Nonnull
    @Override
    public abstract OWLClass getObject();

    @Override
    public OWLClass getEntity() {
        return getObject();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.CLASS;
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
}

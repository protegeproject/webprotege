package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("entity")
public abstract class FormEntitySubject implements FormSubject {

    public static FormEntitySubject get(@Nonnull OWLEntity entity) {
        return new AutoValue_FormEntitySubject(entity);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    @Override
    public IRI getIri() {
        return getEntity().getIRI();
    }
}

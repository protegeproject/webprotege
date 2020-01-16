package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("iri")
public abstract class FormIriSubject implements FormSubject {

    public static FormIriSubject get(@Nonnull IRI iri) {
        return new AutoValue_FormIriSubject(iri);
    }

    @Override
    @Nonnull
    public abstract IRI getIri();

    @Override
    public <R> R accept(@Nonnull FormDataSubjectVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormDataSubjectVisitor visitor) {
        visitor.visit(this);
    }
}

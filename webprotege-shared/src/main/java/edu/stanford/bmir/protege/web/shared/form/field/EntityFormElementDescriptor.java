package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-07
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class EntityFormElementDescriptor {

    @Nonnull
    public abstract OWLProperty getProperty();

    @JsonUnwrapped
    @Nonnull
    public abstract FormElementDescriptor getDescriptor();
}

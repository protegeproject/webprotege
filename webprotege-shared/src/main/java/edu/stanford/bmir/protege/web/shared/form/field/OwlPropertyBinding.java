package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(OwlPropertyBinding.TYPE)
public abstract class OwlPropertyBinding implements OwlBinding {

    public static final String TYPE = "PROPERTY";

    @JsonCreator
    public static OwlPropertyBinding get(@JsonProperty("property") @Nonnull OWLProperty property) {
        return new AutoValue_OwlPropertyBinding(property);
    }

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    @JsonIgnore
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.of(getProperty());
    }
}

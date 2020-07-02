package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(OwlSubClassBinding.TYPE)
public abstract class OwlSubClassBinding implements OwlBinding {

    public static final String TYPE = "SUBCLASS";

    @JsonCreator
    @Nonnull
    public static OwlSubClassBinding get() {
        return new AutoValue_OwlSubClassBinding();
    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }
}

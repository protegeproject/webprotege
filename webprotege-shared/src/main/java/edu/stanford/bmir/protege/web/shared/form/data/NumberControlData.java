package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class NumberControlData implements FormControlData {

    @JsonCreator
    public static NumberControlData get(@Nonnull NumberControlDescriptor descriptor,
                                        @Nullable Double value) {
        return new AutoValue_NumberControlData(descriptor, value);
    }

    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @JsonProperty("value")
    @Nullable
    protected abstract Double getValueInternal();

    @Nonnull
    @JsonIgnore
    public Optional<Double> getValue() {
        return Optional.ofNullable(getValueInternal());
    }
}

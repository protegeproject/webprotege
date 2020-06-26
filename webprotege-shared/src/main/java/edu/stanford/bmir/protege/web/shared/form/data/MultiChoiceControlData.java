package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class MultiChoiceControlData implements FormControlData {

    public static MultiChoiceControlData get(@JsonProperty("descriptor") @Nonnull MultiChoiceControlDescriptor descriptor,
                                             @JsonProperty("values") @Nonnull ImmutableList<PrimitiveFormControlData> values) {
        return new AutoValue_MultiChoiceControlData(descriptor, values);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract MultiChoiceControlDescriptor getDescriptor();

    @JsonProperty("values")
    @Nonnull
    public abstract ImmutableList<PrimitiveFormControlData> getValues();
}

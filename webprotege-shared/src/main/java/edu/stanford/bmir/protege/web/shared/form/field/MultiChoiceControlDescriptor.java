package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(MultiChoiceControlDescriptor.TYPE)
public abstract class MultiChoiceControlDescriptor implements FormControlDescriptor {

    @Nonnull
    public static final String TYPE = "MULTI_CHOICE";

    @JsonCreator
    public static MultiChoiceControlDescriptor get(@JsonProperty("choices") @Nullable ImmutableList<ChoiceDescriptor> choices,
                                                   @JsonProperty("defaultChoices") @Nullable ImmutableList<ChoiceDescriptor> defaultChoices) {

        return new AutoValue_MultiChoiceControlDescriptor(choices == null ? ImmutableList.of() : choices,
                                                          defaultChoices == null ? ImmutableList.of() : defaultChoices);
    }

    @Nonnull
    public abstract ImmutableList<ChoiceDescriptor> getChoices();

    @Nonnull
    public abstract ImmutableList<ChoiceDescriptor> getDefaultChoices();


    @JsonIgnore
    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

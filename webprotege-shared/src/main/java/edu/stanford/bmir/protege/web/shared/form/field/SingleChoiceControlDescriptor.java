package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(SingleChoiceControlDescriptor.TYPE)
@AutoValue
@GwtCompatible(serializable = true)
public abstract class SingleChoiceControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "SINGLE_CHOICE";

    @JsonCreator
    protected static SingleChoiceControlDescriptor get(@JsonProperty("widgetType") @Nullable SingleChoiceControlType widgetType,
                                                       @JsonProperty("defaultChoice") @Nullable ChoiceDescriptor defaultChoice,
                                                       @JsonProperty("choices") @Nonnull ImmutableList<ChoiceDescriptor> choices) {
        return new AutoValue_SingleChoiceControlDescriptor(widgetType == null ? SingleChoiceControlType.COMBO_BOX : widgetType,
                                                           choices,
                                                           defaultChoice);
    }

    @Nonnull
    public static SingleChoiceControlDescriptor get(@Nonnull SingleChoiceControlType widgetType,
                                                    @Nonnull ImmutableList<ChoiceDescriptor> choices,
                                                    @Nonnull ChoiceDescriptor defaultChoice) {
        return new AutoValue_SingleChoiceControlDescriptor(widgetType, choices, defaultChoice);
    }

    @Nonnull
    public static SingleChoiceControlDescriptor get(@Nonnull SingleChoiceControlType widgetType,
                                                    @Nonnull ImmutableList<ChoiceDescriptor> choices) {
        return new AutoValue_SingleChoiceControlDescriptor(widgetType, choices, null);
    }

    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    public abstract SingleChoiceControlType getWidgetType();

    @Nonnull
    public abstract List<ChoiceDescriptor> getChoices();

    @JsonProperty("defaultChoice")
    @Nullable
    protected abstract ChoiceDescriptor getDefaultChoiceInternal();

    @Nonnull
    public Optional<ChoiceDescriptor> getDefaultChoice() {
        return Optional.ofNullable(getDefaultChoiceInternal());
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

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

    private static final String DEFAULT_CHOICE = "defaultChoice";

    private static final String SOURCE = "source";

    private static final String WIDGET_TYPE = "widgetType";

    @JsonCreator
    protected static SingleChoiceControlDescriptor get(@JsonProperty(WIDGET_TYPE) @Nullable SingleChoiceControlType widgetType,
                                                       @JsonProperty(DEFAULT_CHOICE) @Nullable ChoiceDescriptor defaultChoice,
                                                       @JsonProperty(SOURCE) @Nullable ChoiceListSourceDescriptor source) {
        return new AutoValue_SingleChoiceControlDescriptor(widgetType == null ? SingleChoiceControlType.COMBO_BOX : widgetType,
                                                           source == null ? FixedChoiceListSourceDescriptor.get(ImmutableList.of()) : source,
                                                           defaultChoice);
    }

    @Nonnull
    public static SingleChoiceControlDescriptor get(@Nonnull SingleChoiceControlType widgetType,
                                                    @Nonnull ChoiceListSourceDescriptor source,
                                                    @Nonnull ChoiceDescriptor defaultChoice) {
        return new AutoValue_SingleChoiceControlDescriptor(widgetType, source, defaultChoice);
    }

    @Nonnull
    public static SingleChoiceControlDescriptor get(@Nonnull SingleChoiceControlType widgetType,
                                                    @Nonnull ChoiceListSourceDescriptor source) {
        return new AutoValue_SingleChoiceControlDescriptor(widgetType, source, null);
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

    @JsonProperty(WIDGET_TYPE)
    @Nonnull
    public abstract SingleChoiceControlType getWidgetType();

    @JsonProperty(SOURCE)
    @Nonnull
    public abstract ChoiceListSourceDescriptor getSource();

    @JsonProperty(DEFAULT_CHOICE)
    @Nullable
    protected abstract ChoiceDescriptor getDefaultChoiceInternal();

    @JsonIgnore
    @Nonnull
    public Optional<ChoiceDescriptor> getDefaultChoice() {
        return Optional.ofNullable(getDefaultChoiceInternal());
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

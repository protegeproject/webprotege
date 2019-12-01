package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(ChoiceControlDescriptor.TYPE)
public class ChoiceControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "CHOICE";

    private ChoiceFieldType widgetType = ChoiceFieldType.COMBO_BOX;

    private List<ChoiceDescriptor> choices = new ArrayList<>();

    @Nonnull
    private List<FormDataValue> defaultChoices = new ArrayList<>();

    private ChoiceControlDescriptor() {
    }

    public ChoiceControlDescriptor(@Nonnull ChoiceFieldType widgetType,
                                   @Nonnull List<ChoiceDescriptor> choices,
                                   @Nonnull List<FormDataValue> defaultChoices) {
        this.widgetType = checkNotNull(widgetType);
        this.choices.addAll(checkNotNull(choices));
        this.defaultChoices = checkNotNull(defaultChoices);
    }

    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ChoiceControlDescriptor)) {
            return false;
        }
        ChoiceControlDescriptor other = (ChoiceControlDescriptor) obj;
        return this.widgetType.equals(other.widgetType)
                && this.choices.equals(other.choices);
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    public List<ChoiceDescriptor> getChoices() {
        return new ArrayList<>(choices);
    }

    @Nonnull
    public List<FormDataValue> getDefaultChoices() {
        return new ArrayList<>(defaultChoices);
    }

    @Nonnull
    public ChoiceFieldType getWidgetType() {
        return widgetType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(widgetType, choices);
    }

    @Override
    public String toString() {
        return toStringHelper("ChoiceControlDescriptor")
                .add("widgetType", widgetType)
                .add("choices", choices)
                .toString();
    }
}

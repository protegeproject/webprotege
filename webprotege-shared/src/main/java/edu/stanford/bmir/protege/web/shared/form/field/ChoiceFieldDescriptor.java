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
@JsonTypeName(ChoiceFieldDescriptor.TYPE)
public class ChoiceFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "Choice";

    private ChoiceFieldType widgetType = ChoiceFieldType.COMBO_BOX;

    private List<ChoiceDescriptor> choices = new ArrayList<>();

    @Nonnull
    private List<FormDataValue> defaultChoices = new ArrayList<>();

    private ChoiceFieldDescriptor() {
    }

    public ChoiceFieldDescriptor(@Nonnull ChoiceFieldType widgetType,
                                 @Nonnull List<ChoiceDescriptor> choices,
                                 @Nonnull List<FormDataValue> defaultChoices) {
        this.widgetType = checkNotNull(widgetType);
        this.choices.addAll(checkNotNull(choices));
        this.defaultChoices = checkNotNull(defaultChoices);
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    public static String getType() {
        return TYPE;
    }

    @Nonnull
    public ChoiceFieldType getWidgetType() {
        return widgetType;
    }

    @Nonnull
    public List<ChoiceDescriptor> getChoices() {
        return new ArrayList<>(choices);
    }

    @Nonnull
    public List<FormDataValue> getDefaultChoices() {
        return new ArrayList<>(defaultChoices);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(widgetType, choices);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChoiceFieldDescriptor)) {
            return false;
        }
        ChoiceFieldDescriptor other = (ChoiceFieldDescriptor) obj;
        return this.widgetType.equals(other.widgetType)
                && this.choices.equals(other.choices);
    }


    @Override
    public String toString() {
        return toStringHelper("ChoiceFieldDescriptor")
                .add("widgetType", widgetType)
                .add("choices", choices)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(ChoiceFieldDescriptor.TYPE)
public class ChoiceFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "Choice";

    private ChoiceFieldType widgetType;

    private List<ChoiceDescriptor> choiceDescriptors = new ArrayList<>();

    private ChoiceFieldDescriptor() {
    }

    public ChoiceFieldDescriptor(ChoiceFieldType widgetType, List<ChoiceDescriptor> choiceDescriptors) {
        this.widgetType = widgetType;
        this.choiceDescriptors.addAll(choiceDescriptors);
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

    public ChoiceFieldType getWidgetType() {
        return widgetType;
    }

    public List<ChoiceDescriptor> getChoiceDescriptors() {
        return new ArrayList<>(choiceDescriptors);
    }
}

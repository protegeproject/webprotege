package edu.stanford.bmir.protege.web.shared.form.field;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class ChoiceFieldDescriptor implements FormFieldDescriptor {

    private static final String FIELD_TYPE_ID = "ChoiceField";

    private ChoiceFieldType type;

    private List<ChoiceDescriptor> choiceDescriptors = new ArrayList<>();

    private ChoiceFieldDescriptor() {
    }

    public ChoiceFieldDescriptor(ChoiceFieldType type, List<ChoiceDescriptor> choiceDescriptors) {
        this.type = type;
        this.choiceDescriptors.addAll(choiceDescriptors);
    }

    @Override
    public String getAssociatedFieldTypeId() {
        return FIELD_TYPE_ID;
    }

    public static String getFieldTypeId() {
        return FIELD_TYPE_ID;
    }

    public ChoiceFieldType getType() {
        return type;
    }

    public List<ChoiceDescriptor> getChoiceDescriptors() {
        return new ArrayList<>(choiceDescriptors);
    }
}

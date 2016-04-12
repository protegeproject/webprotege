package edu.stanford.bmir.protege.web.shared.form.field;

import edu.stanford.bmir.protege.web.shared.form.HasFormElementId;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementDescriptor implements HasFormElementId, HasRepeatability, Serializable {

    private FormElementId id;

    private String label;

    private FormFieldDescriptor fieldDescriptor;

    private Repeatability repeatability;

    private Required required;

    private FormElementDescriptor() {
    }

    public FormElementDescriptor(FormElementId id,
                                 String formLabel,
                                 FormFieldDescriptor fieldDescriptor,
                                 Repeatability repeatability,
                                 Required required) {
        this.id = id;
        this.label = formLabel;
        this.repeatability = repeatability;
        this.fieldDescriptor = fieldDescriptor;
        this.required = required;
    }

    @Override
    public FormElementId getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public Repeatability getRepeatability() {
        return repeatability;
    }

    public Required getRequired() {
        return required;
    }

    public FormFieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }
}

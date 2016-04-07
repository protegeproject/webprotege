package edu.stanford.bmir.protege.web.shared.form.field;

import edu.stanford.bmir.protege.web.shared.form.HasFormElementId;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementDescriptor implements HasFormElementId, HasRepeatability, Serializable {

    private FormElementId formElementId;

    private String formLabel;

    private Repeatability repeatability;

    private FormFieldDescriptor formFieldDescriptor;

    private FormElementDescriptor() {
    }

    public FormElementDescriptor(FormElementId formElementId, String formLabel, Repeatability repeatability, FormFieldDescriptor formFieldDescriptor) {
        this.formElementId = formElementId;
        this.formLabel = formLabel;
        this.repeatability = repeatability;
        this.formFieldDescriptor = formFieldDescriptor;
    }

    @Override
    public FormElementId getFormElementId() {
        return formElementId;
    }

    public String getFormLabel() {
        return formLabel;
    }

    @Override
    public Repeatability getRepeatability() {
        return repeatability;
    }

    public FormFieldDescriptor getFormFieldDescriptor() {
        return formFieldDescriptor;
    }
}

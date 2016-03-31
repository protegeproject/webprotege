package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor {

    private List<FormElementDescriptor> elementDescriptors = new ArrayList<>();

    public FormDescriptor(List<FormElementDescriptor> fieldDescriptors) {
        this.elementDescriptors.addAll(fieldDescriptors);
    }

    public List<FormElementDescriptor> getFieldDescriptors() {
        return new ArrayList<>(elementDescriptors);
    }
}

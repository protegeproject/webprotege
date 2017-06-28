package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor implements Serializable {

    @JsonUnwrapped
    private FormId id;

    private List<FormElementDescriptor> elements;

    private FormDescriptor() {
    }

    public FormDescriptor(FormId id, List<FormElementDescriptor> formElementDescriptors) {
        this.id = id;
        this.elements = new ArrayList<>(formElementDescriptors);
    }

    public static FormDescriptor empty() {
        return new FormDescriptor(new FormId("EmptyForm"), Collections.emptyList());
    }

    public FormId getId() {
        return id;
    }

    public List<FormElementDescriptor> getElements() {
        return elements;
    }

    public static Builder builder(FormId formId) {
        return new Builder(formId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormDescriptor)) {
            return false;
        }
        FormDescriptor other = (FormDescriptor) obj;
        return this.id.equals(other.id)
                && this.elements.equals(other.elements);
    }

    public static class Builder {

        private final FormId formId;

        private final List<FormElementDescriptor> builder_elementDescriptors = new ArrayList<>();


        public Builder(FormId formId) {
            this.formId = checkNotNull(formId);
        }

        public Builder addDescriptor(FormElementDescriptor descriptor) {
            builder_elementDescriptors.add(descriptor);
            return this;
        }


        public FormDescriptor build() {
            return new FormDescriptor(formId, builder_elementDescriptors);
        }
    }
}

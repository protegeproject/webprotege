package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor implements Serializable {

    private FormId id;

    private List<FormElementDescriptor> elementDescriptors;

    private FormDescriptor() {
    }

    public FormDescriptor(FormId id, List<FormElementDescriptor> formElementDescriptors) {
        this.id = id;
        this.elementDescriptors = new ArrayList<>(formElementDescriptors);
    }

    public static FormDescriptor empty() {
        return new FormDescriptor(new FormId("EmptyForm"), Collections.emptyList());
    }

    public FormId getId() {
        return id;
    }

    public List<FormElementDescriptor> getFormElementDescriptors() {
        return elementDescriptors;
    }

    public static Builder builder(FormId formId) {
        return new Builder(formId);
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

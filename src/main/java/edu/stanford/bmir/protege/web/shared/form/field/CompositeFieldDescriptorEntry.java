package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class CompositeFieldDescriptorEntry implements Serializable, IsSerializable {

    private FormElementId elementId;

    private FormFieldDescriptor descriptor;

    // Weight?

    private CompositeFieldDescriptorEntry() {
    }

    public CompositeFieldDescriptorEntry(FormElementId elementId, FormFieldDescriptor descriptor) {
        this.elementId = checkNotNull(elementId);
        this.descriptor = checkNotNull(descriptor);
    }

    public FormElementId getElementId() {
        return elementId;
    }

    public FormFieldDescriptor getDescriptor() {
        return descriptor;
    }
}

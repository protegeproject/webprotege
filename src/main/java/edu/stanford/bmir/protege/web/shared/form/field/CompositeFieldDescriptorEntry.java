package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.base.Objects;
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

    private double flexGrow;

    private double flexShrink;

    private CompositeFieldDescriptorEntry() {
    }

    public CompositeFieldDescriptorEntry(FormElementId elementId,
                                         double flexGrow,
                                         double flexShrink,
                                         FormFieldDescriptor descriptor) {
        this.elementId = checkNotNull(elementId);
        this.descriptor = checkNotNull(descriptor);
        this.flexGrow = flexGrow;
        this.flexShrink = flexShrink;
    }

    public FormElementId getElementId() {
        return elementId;
    }

    public FormFieldDescriptor getDescriptor() {
        return descriptor;
    }

    public double getFlexGrow() {
        return flexGrow;
    }

    public double getFlexShrink() {
        return flexShrink;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elementId, descriptor, flexGrow, flexShrink);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CompositeFieldDescriptorEntry)) {
            return false;
        }
        CompositeFieldDescriptorEntry other = (CompositeFieldDescriptorEntry) obj;
        return this.elementId.equals(other.elementId)
                && this.descriptor.equals(other.descriptor)
                && this.flexGrow == other.flexGrow
                && this.flexShrink == other.flexShrink;
    }
}

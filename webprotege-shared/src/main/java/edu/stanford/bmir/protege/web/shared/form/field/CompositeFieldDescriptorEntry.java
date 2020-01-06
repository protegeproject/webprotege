package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
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

    private FormFieldDescriptor descriptor;

    private double flexBasis;

    private double flexGrow;

    private double flexShrink;


    private CompositeFieldDescriptorEntry() {
    }

    public CompositeFieldDescriptorEntry(double flexGrow,
                                         double flexShrink,
                                         FormFieldDescriptor descriptor, double flexBasis) {
        this.descriptor = checkNotNull(descriptor);
        this.flexGrow = flexGrow;
        this.flexShrink = flexShrink;
        this.flexBasis = flexBasis;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof CompositeFieldDescriptorEntry)) {
            return false;
        }
        CompositeFieldDescriptorEntry other = (CompositeFieldDescriptorEntry) obj;
        return this.descriptor.equals(other.descriptor)
                && this.flexBasis == other.flexBasis
                && this.flexGrow == other.flexGrow
                && this.flexShrink == other.flexShrink;
    }

    public double getBasis() {
        return flexBasis;
    }

    @JsonUnwrapped
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
        return Objects.hashCode(descriptor, flexBasis, flexGrow, flexShrink);
    }
}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class CompositeFieldDescriptor implements FormFieldDescriptor {

    private static final String COMPOSITE_FIELD = "CompositeField";

    public static String getFieldTypeId() {
        return COMPOSITE_FIELD;
    }

    // Direction?

    private List<CompositeFieldDescriptorEntry> childDescriptors = new ArrayList<>();

    private CompositeFieldDescriptor() {
    }

    public CompositeFieldDescriptor(List<CompositeFieldDescriptorEntry> childDescriptors) {
        this.childDescriptors.addAll(childDescriptors);
    }

    public List<CompositeFieldDescriptorEntry> getChildDescriptors() {
        return new ArrayList<>(childDescriptors);
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return COMPOSITE_FIELD;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(childDescriptors);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CompositeFieldDescriptor)) {
            return false;
        }
        CompositeFieldDescriptor other = (CompositeFieldDescriptor) obj;
        return this.childDescriptors.equals(other.childDescriptors);
    }
}

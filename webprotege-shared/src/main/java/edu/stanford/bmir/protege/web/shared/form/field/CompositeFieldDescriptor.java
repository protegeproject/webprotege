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

    public static final String TYPE = "COMPOSITE";

    public static String getFieldTypeId() {
        return TYPE;
    }

    // Direction?

    private List<CompositeFieldDescriptorEntry> elements = new ArrayList<>();

    private CompositeFieldDescriptor() {
    }

    public CompositeFieldDescriptor(List<CompositeFieldDescriptorEntry> elements) {
        this.elements.addAll(elements);
    }

    public List<CompositeFieldDescriptorEntry> getElements() {
        return new ArrayList<>(elements);
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
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
        return this.elements.equals(other.elements);
    }
}

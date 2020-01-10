package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/04/16
 */
@JsonTypeName(ImageControlDescriptor.TYPE)
public class ImageControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "IMAGE";

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ImageControlDescriptor)) {
            return false;
        }
        ImageControlDescriptor other = (ImageControlDescriptor) obj;
        return true;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    @Override
    public int hashCode() {
        return 33;
    }

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

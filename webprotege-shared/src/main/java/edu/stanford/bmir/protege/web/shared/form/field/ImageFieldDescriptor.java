package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/04/16
 */
@JsonTypeName(ImageFieldDescriptor.TYPE)
public class ImageFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "Image";

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Override
    public int hashCode() {
        return 33;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ImageFieldDescriptor)) {
            return false;
        }
        ImageFieldDescriptor other = (ImageFieldDescriptor) obj;
        return true;
    }
}

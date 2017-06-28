package edu.stanford.bmir.protege.web.shared.form.field;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/04/16
 */
public class ImageFieldDescriptor implements FormFieldDescriptor {

    private static final String IMAGE_FIELD = "ImageField";

    @Nonnull
    @Override
    public String getAssociatedType() {
        return IMAGE_FIELD;
    }

    public static String getFieldTypeId() {
        return IMAGE_FIELD;
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

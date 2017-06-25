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
}

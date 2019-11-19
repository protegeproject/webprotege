package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormFieldDescriptorPresenterFactory {

    @Nonnull
    String getDescriptorType();

    @Nonnull
    FormFieldDescriptor createDefaultDescriptor();

    @Nonnull
    FormFieldDescriptorPresenter create();
}

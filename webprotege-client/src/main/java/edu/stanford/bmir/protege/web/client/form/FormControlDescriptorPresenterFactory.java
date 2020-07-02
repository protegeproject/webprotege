package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormControlDescriptorPresenterFactory {

    @Nonnull
    String getDescriptorLabel();

    @Nonnull
    String getDescriptorType();

    @Nonnull
    FormControlDescriptor createDefaultDescriptor();

    @Nonnull
    FormControlDescriptorPresenter create();
}

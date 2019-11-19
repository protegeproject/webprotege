package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ImageFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-19
 */
public class ImageDescriptorPresenterFactory implements FormFieldDescriptorPresenterFactory {

    @Nonnull
    private final Provider<ImageDescriptorPresenter> presenterProvider;

    @Inject
    public ImageDescriptorPresenterFactory(@Nonnull Provider<ImageDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Image";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return ImageFieldDescriptor.getFieldTypeId();
    }

    @Nonnull
    @Override
    public FormFieldDescriptor createDefaultDescriptor() {
        return new ImageFieldDescriptor();
    }

    @Nonnull
    @Override
    public FormFieldDescriptorPresenter create() {
        return presenterProvider.get();
    }
}

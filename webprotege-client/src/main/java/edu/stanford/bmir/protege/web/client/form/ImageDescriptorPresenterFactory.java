package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ImageControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-19
 */
public class ImageDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {

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
        return ImageControlDescriptor.getFieldTypeId();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return new ImageControlDescriptor();
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}

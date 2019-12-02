package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ImageControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-19
 */
public class ImageDescriptorPresenter implements FormControlDescriptorPresenter {

    private final ImageDescriptorView view;

    @Inject
    public ImageDescriptorPresenter(@Nonnull ImageDescriptorView view) {
        this.view = view;
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        return new ImageControlDescriptor();
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}

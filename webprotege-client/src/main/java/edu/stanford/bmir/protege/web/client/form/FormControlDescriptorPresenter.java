package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormControlDescriptorPresenter {

    @Nonnull
    FormControlDescriptor getFormFieldDescriptor();

    void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor);

    void clear();

    void start(@Nonnull AcceptsOneWidget container);
}

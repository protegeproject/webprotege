package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormFieldDescriptorPresenter {

    @Nonnull
    FormFieldDescriptor getFormFieldDescriptor();

    void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor);

    void clear();

    void start(@Nonnull AcceptsOneWidget container);
}

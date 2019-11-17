package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormFieldDescriptorEditorPresenter {

    @Nonnull
    FormFieldDescriptor getFormFieldDescriptor();

    void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor);

    void clear();

    void start(@Nonnull AcceptsOneWidget container);
}

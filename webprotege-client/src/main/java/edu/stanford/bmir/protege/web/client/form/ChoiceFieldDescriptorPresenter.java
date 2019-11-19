package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ChoiceFieldDescriptorPresenter implements FormFieldDescriptorPresenter {

    @Nonnull
    private final ChoiceFieldDescriptorView view;

    @Inject
    public ChoiceFieldDescriptorPresenter(@Nonnull ChoiceFieldDescriptorView view) {
        this.view = checkNotNull(view);
    }

    @Nonnull
    @Override
    public FormFieldDescriptor getFormFieldDescriptor() {
        return null;
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        if(!(formFieldDescriptor instanceof ChoiceFieldDescriptor)) {
            return;
        }
        ChoiceFieldDescriptor choiceFieldDescriptor = (ChoiceFieldDescriptor) formFieldDescriptor;
        view.setWidgetType(choiceFieldDescriptor.getWidgetType());

    }

    @Override
    public void clear() {

    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ChoiceFieldDescriptorPresenter implements FormFieldDescriptorPresenter {

    private static final ChoiceFieldDescriptor DEFAULT_DESCRIPTOR = new ChoiceFieldDescriptor(ChoiceFieldType.SEGMENTED_BUTTON,
                                                                                              Collections.emptyList(),
                                                                                              Collections.emptyList());

    @Nonnull
    private final ChoiceFieldDescriptorView view;

    @Inject
    public ChoiceFieldDescriptorPresenter(@Nonnull ChoiceFieldDescriptorView view) {
        this.view = checkNotNull(view);
    }

    @Nonnull
    @Override
    public FormFieldDescriptor getFormFieldDescriptor() {
        return new ChoiceFieldDescriptor(
                view.getWidgetType(),
                view.getChoiceDescriptors(),
                Collections.emptyList()
        );
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        checkNotNull(formFieldDescriptor);
        if(!(formFieldDescriptor instanceof ChoiceFieldDescriptor)) {
            return;
        }
        updateView((ChoiceFieldDescriptor) formFieldDescriptor);

    }

    public void updateView(@Nonnull ChoiceFieldDescriptor formFieldDescriptor) {
        view.setWidgetType(formFieldDescriptor.getWidgetType());
        view.setChoiceDescriptors(formFieldDescriptor.getChoices());
    }

    @Override
    public void clear() {
        updateView(DEFAULT_DESCRIPTOR);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}

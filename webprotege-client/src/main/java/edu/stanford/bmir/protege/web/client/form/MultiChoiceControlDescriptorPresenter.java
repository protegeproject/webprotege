package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-10
 */
public class MultiChoiceControlDescriptorPresenter implements FormControlDescriptorPresenter {

    @Nonnull
    private final MultiChoiceControlDescriptorView view;

    @Inject
    public MultiChoiceControlDescriptorPresenter(@Nonnull MultiChoiceControlDescriptorView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public void clear() {
        view.setChoiceDescriptors(Collections.emptyList());
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        ImmutableList<ChoiceDescriptor> choiceDescriptors = view.getChoiceDescriptors();
        return MultiChoiceControlDescriptor.get(choiceDescriptors,
                                                ImmutableList.of());
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        if(formControlDescriptor instanceof MultiChoiceControlDescriptor) {
            MultiChoiceControlDescriptor descriptor = (MultiChoiceControlDescriptor) formControlDescriptor;
            view.setChoiceDescriptors(descriptor.getChoices());
        }
        else {
            clear();
        }
    }

}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormFieldDescriptorPresenter implements FormFieldDescriptorPresenter {

    @Nonnull
    private final FormDescriptorPresenter subFormPresenter;

    @Nonnull
    private final SubFormFieldDescriptorView view;

    @Inject
    public SubFormFieldDescriptorPresenter(@Nonnull FormDescriptorPresenter subFormPresenter,
                                           @Nonnull SubFormFieldDescriptorView view) {
        this.subFormPresenter = checkNotNull(subFormPresenter);
        this.view = checkNotNull(view);
    }

    @Nonnull
    @Override
    public FormFieldDescriptor getFormFieldDescriptor() {
        try {
            FormDescriptor subFormDescriptor = subFormPresenter.getFormDescriptor();
            return new SubFormFieldDescriptor(subFormDescriptor);
        } catch(Exception e) {
            Window.alert(e.getClass().getName());
            return new SubFormFieldDescriptor(FormDescriptor.empty());
        }
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        if(!(formFieldDescriptor instanceof SubFormFieldDescriptor)) {
            return;
        }
        SubFormFieldDescriptor subFormFieldDescriptor = (SubFormFieldDescriptor) formFieldDescriptor;
        subFormPresenter.setFormDescriptor(subFormFieldDescriptor.getFormDescriptor());
    }

    @Override
    public void clear() {
        view.clear();
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        subFormPresenter.start(view.getSubFormContainer(), new SimpleEventBus());
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FixedChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class SingleControlDescriptorPresenter implements FormControlDescriptorPresenter {

    private static final SingleChoiceControlDescriptor DEFAULT_DESCRIPTOR = SingleChoiceControlDescriptor.get(
            SingleChoiceControlType.SEGMENTED_BUTTON,
            FixedChoiceListSourceDescriptor.get(ImmutableList.of()));

    @Nonnull
    private final SingleChoiceControlDescriptorView view;

    @Nonnull
    private final ChoiceListSourceDescriptorPresenter choiceListSourceDescriptorPresenter;

    @Inject
    public SingleControlDescriptorPresenter(@Nonnull SingleChoiceControlDescriptorView view,
                                            @Nonnull ChoiceListSourceDescriptorPresenter choiceListSourceDescriptorPresenter) {
        this.view = checkNotNull(view);
        this.choiceListSourceDescriptorPresenter = checkNotNull(choiceListSourceDescriptorPresenter);
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        return SingleChoiceControlDescriptor.get(
                view.getWidgetType(),
                choiceListSourceDescriptorPresenter.getDescriptor()
        );
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        checkNotNull(formControlDescriptor);
        if(!(formControlDescriptor instanceof SingleChoiceControlDescriptor)) {
            clear();
            return;
        }
        updateView((SingleChoiceControlDescriptor) formControlDescriptor);

    }

    public void updateView(@Nonnull SingleChoiceControlDescriptor formFieldDescriptor) {
        view.setWidgetType(formFieldDescriptor.getWidgetType());
        choiceListSourceDescriptorPresenter.setDescriptor(formFieldDescriptor.getSource());
    }

    @Override
    public void clear() {
        updateView(DEFAULT_DESCRIPTOR);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        choiceListSourceDescriptorPresenter.start(view.getSourceContainer());
    }
}

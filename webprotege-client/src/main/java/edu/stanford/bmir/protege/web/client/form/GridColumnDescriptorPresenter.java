package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridColumnDescriptorPresenter implements ObjectPresenter<GridColumnDescriptor> {

    @Nonnull
    private final GridColumnDescriptorView view;

    private Optional<GridColumnDescriptor> descriptor = Optional.empty();

    private final FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    private final OwlBindingPresenter bindingPresenter;

    @Inject
    public GridColumnDescriptorPresenter(@Nonnull GridColumnDescriptorView view,
                                         @Nonnull FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter,
                                         @Nonnull OwlBindingPresenter bindingPresenter) {
        this.view = checkNotNull(view);
        this.fieldDescriptorChooserPresenter = checkNotNull(fieldDescriptorChooserPresenter);
        this.bindingPresenter = bindingPresenter;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorChooserContainer());
        bindingPresenter.start(view.getBindingViewContainer());
    }

    public void setValue(@Nonnull GridColumnDescriptor descriptor) {
        this.descriptor = Optional.of(descriptor);
        view.setId(descriptor.getId());
        view.setLabel(descriptor.getLabel());
        bindingPresenter.clear();
        descriptor.getOwlBinding().ifPresent(bindingPresenter::setBinding);
        fieldDescriptorChooserPresenter.setFormFieldDescriptor(descriptor.getFormControlDescriptor());
    }

    @Nonnull
    @Override
    public Optional<GridColumnDescriptor> getValue() {
        return fieldDescriptorChooserPresenter.getFormFieldDescriptor()
                .map(fieldDescriptor -> GridColumnDescriptor.get(view.getId(),
                                                             bindingPresenter.getBinding().orElse(null),
                                                             view.getLabel(),
                                                             fieldDescriptor));
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return view.getId().getId() + " Column";
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {

    }
}

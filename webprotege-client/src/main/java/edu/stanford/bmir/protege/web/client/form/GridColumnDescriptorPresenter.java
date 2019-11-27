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

    private final FormFieldDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    @Inject
    public GridColumnDescriptorPresenter(@Nonnull GridColumnDescriptorView view,
                                         @Nonnull FormFieldDescriptorChooserPresenter fieldDescriptorChooserPresenter) {
        this.view = checkNotNull(view);
        this.fieldDescriptorChooserPresenter = checkNotNull(fieldDescriptorChooserPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorChooserContainer());
    }

    public void setValue(@Nonnull GridColumnDescriptor descriptor) {
        this.descriptor = Optional.of(descriptor);
        view.setId(descriptor.getId());
        view.setLabel(descriptor.getLabel());
    }

    @Nonnull
    @Override
    public Optional<GridColumnDescriptor> getValue() {
        return fieldDescriptorChooserPresenter.getFormFieldDescriptor()
                .map(fieldDescriptor -> GridColumnDescriptor.get(view.getId(),
                                                             null,
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

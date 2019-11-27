package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridFieldDescriptorPresenter implements FormFieldDescriptorPresenter {

    @Nonnull
    private final GridFieldDescriptorView view;

    @Nonnull
    private final ObjectListPresenter<GridColumnDescriptor> columnListPresenter;

    @Inject
    public GridFieldDescriptorPresenter(@Nonnull GridFieldDescriptorView view,
                                        @Nonnull ObjectListPresenter<GridColumnDescriptor> columnListPresenter) {
        this.view = view;
        this.columnListPresenter = columnListPresenter;
    }

    @Nonnull
    @Override
    public FormFieldDescriptor getFormFieldDescriptor() {
        return GridFieldDescriptor.get(ImmutableList.copyOf(columnListPresenter.getValues()));
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        if(!(formFieldDescriptor instanceof GridFieldDescriptor)) {
            return;
        }
        GridFieldDescriptor gridFieldDescriptor = (GridFieldDescriptor) formFieldDescriptor;
        columnListPresenter.setValues(gridFieldDescriptor.getColumns());
    }

    @Override
    public void clear() {

    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        columnListPresenter.start(view.getViewContainer(), new SimpleEventBus());
    }
}

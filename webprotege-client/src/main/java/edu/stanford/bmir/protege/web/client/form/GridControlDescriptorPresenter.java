package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridControlDescriptorPresenter implements FormControlDescriptorPresenter {

    @Nonnull
    private final GridControlDescriptorView view;


    @Nonnull
    private final FormSubjectFactoryDescriptorPresenter formSubjectFactoryDescriptorPresenter;

    @Nonnull
    private final GridColumnDescriptorObjectListPresenter columnListPresenter;


    @Inject
    public GridControlDescriptorPresenter(@Nonnull GridControlDescriptorView view,
                                          @Nonnull FormSubjectFactoryDescriptorPresenter formSubjectFactoryDescriptorPresenter,
                                          @Nonnull GridColumnDescriptorObjectListPresenter columnListPresenter) {
        this.view = view;
        this.formSubjectFactoryDescriptorPresenter = formSubjectFactoryDescriptorPresenter;
        this.columnListPresenter = columnListPresenter;
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        return GridControlDescriptor.get(ImmutableList.copyOf(columnListPresenter.getValues()),
                                         formSubjectFactoryDescriptorPresenter.getDescriptor().orElse(null));
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        if(!(formControlDescriptor instanceof GridControlDescriptor)) {
            return;
        }
        GridControlDescriptor gridFieldDescriptor = (GridControlDescriptor) formControlDescriptor;
        columnListPresenter.setValues(gridFieldDescriptor.getColumns());
        gridFieldDescriptor.getSubjectFactoryDescriptor().ifPresent(formSubjectFactoryDescriptorPresenter::setDescriptor);
    }

    @Override
    public void clear() {

    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        columnListPresenter.start(view.getViewContainer(), new SimpleEventBus());
        columnListPresenter.setAddObjectText("Add column");
        formSubjectFactoryDescriptorPresenter.start(view.getFormSubjectFactoryDescriptorContainer());
        columnListPresenter.setDefaultStateCollapsed();
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.Optionality.REQUIRED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridCellPresenter implements HasRequestFocus, HasFormRegionPagedChangedHandler, HasGridColumnVisibilityManager {

    @Nonnull
    private final GridCellView view;

    @Nonnull
    private final GridColumnDescriptorDto descriptor;

    @Nonnull
    private final FormControlStackPresenter stackPresenter;

    private Optional<GridColumnVisibilityManager> columnVisibilityManager = Optional.empty();

    @AutoFactory
    public GridCellPresenter(@Provided @Nonnull GridCellView view,
                             @Nonnull GridColumnDescriptorDto columnDescriptor,
                             @Nonnull FormControlStackPresenter stackPresenter) {
        this.view = checkNotNull(view);
        this.descriptor = checkNotNull(columnDescriptor);
        this.stackPresenter = checkNotNull(stackPresenter);
    }

    public void clear() {
        stackPresenter.clearValue();
        updateValueRequired();
    }

    @Nonnull
    public ImmutableList<FormRegionPageRequest> getPageRequest() {
        return ImmutableList.of();
    }

    public void setEnabled(boolean enabled) {
        stackPresenter.setEnabled(enabled);
    }

    @Override
    public void setRegionPageChangedHandler(@Nonnull RegionPageChangedHandler handler) {
        stackPresenter.setRegionPageChangedHandler(handler);
    }

    public GridColumnId getId() {
        return descriptor.getId();
    }

    public void requestFocus() {
        view.requestFocus();
        stackPresenter.requestFocus();
    }

    public void start(AcceptsOneWidget cellContainer) {
        stackPresenter.start(view.getEditorContainer());
        cellContainer.setWidget(view);
    }

    public void setValue(GridCellDataDto data) {
        stackPresenter.setValue(data.getValues());
        updateValueRequired();
        updateColumnVisibilityManagerInChildControls();
    }

    public GridCellData getValue() {
        ImmutableList<FormControlData> values = stackPresenter.getValue();
        return GridCellData.get(descriptor.getId(),
                                Page.of(values));
    }

    private void updateValueRequired() {
        if (descriptor.getOptionality() == REQUIRED) {
            boolean requiredValueNotPresent = stackPresenter.getValue().isEmpty();
            view.setRequiredValueNotPresentVisible(requiredValueNotPresent);
        }
        else {
            view.setRequiredValueNotPresentVisible(false);
        }

    }

    public void updateColumnVisibility() {
        stackPresenter.forEachFormControl(formControl -> {
            if(formControl instanceof GridControl) {
                ((GridControl) formControl).updateColumnVisibility();
            }
        });
    }

    @Override
    public void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager) {
        this.columnVisibilityManager = Optional.of(checkNotNull(columnVisibilityManager));
        updateColumnVisibilityManagerInChildControls();
    }

    private void updateColumnVisibilityManagerInChildControls() {
        columnVisibilityManager.ifPresent(visibilityManager -> {
            stackPresenter.forEachFormControl(formControl -> {
                if(formControl instanceof GridControl) {
                    ((GridControl) formControl).setColumnVisibilityManager(visibilityManager);
                }
            });
        });
    }

}

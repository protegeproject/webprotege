package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridControl implements FormControl, HasGridColumnVisibilityManager {

    private final HandlerManager handlerManager = new HandlerManager(this);

    private final SimplePanel view = new SimplePanel();

    @Nonnull
    private final GridPresenter gridPresenter;

    @Inject
    public GridControl(@Nonnull GridPresenter gridPresenter) {
        this.gridPresenter = checkNotNull(gridPresenter);
        this.gridPresenter.start(view);
    }

    @Override
    public void setRegionPosition(@Nonnull FormRegionPosition position) {
        if(position.equals(FormRegionPosition.NESTED)) {
            this.gridPresenter.hideHeaderRow();
        }
        else {
            gridPresenter.setColumnVisibilityManager(new GridColumnVisibilityManager());
            this.gridPresenter.setTopLevel();
        }
    }

    public void setDescriptor(@Nullable GridControlDescriptor descriptor) {
        gridPresenter.setDescriptor(descriptor);
    }

    @Override
    public void setValue(@Nonnull FormControlDataDto object) {
        if(object instanceof GridControlDataDto) {
            gridPresenter.setValue((GridControlDataDto) object);
        }
        else {
            gridPresenter.clearValue();
        }
    }

    @Override
    public void clearValue() {
        gridPresenter.clearValue();
    }

    @Override
    public Widget asWidget() {
        return view;
    }

    @Override
    public Optional<FormControlData> getValue() {
        GridControlData value = gridPresenter.getValue();
        return Optional.of(value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public void requestFocus() {
        gridPresenter.requestFocus();
    }

    @Nonnull
    @Override
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject,
                                                                @Nonnull FormRegionId formRegionId) {
        return gridPresenter.getPageRequests(formSubject, formRegionId);
    }

    @Override
    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {
        gridPresenter.setFormRegionPageChangedHandler(handler);
    }

    @Override
    public void setEnabled(boolean enabled) {
        gridPresenter.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return gridPresenter.isEnabled();
    }

    @Override
    public void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager) {
        gridPresenter.setColumnVisibilityManager(columnVisibilityManager);
    }

    public void updateColumnVisibility() {
        gridPresenter.updateVisibleColumns();
    }
}

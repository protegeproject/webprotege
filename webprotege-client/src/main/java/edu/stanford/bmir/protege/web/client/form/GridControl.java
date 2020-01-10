package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.GridControlData;
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
public class GridControl implements FormControl {

    private final HandlerManager handlerManager = new HandlerManager(this);

    private final SimplePanel view = new SimplePanel();

    @Nonnull
    private final GridPresenter gridPresenter;

    @Inject
    public GridControl(@Nonnull GridPresenter gridPresenter) {
        this.gridPresenter = checkNotNull(gridPresenter);
        this.gridPresenter.start(view);
    }

    public void setDescriptor(@Nullable GridControlDescriptor descriptor) {
        gridPresenter.setDescriptor(descriptor);
    }

    @Override
    public void setValue(FormControlData object) {
        if(object instanceof GridControlData) {
            gridPresenter.setValue((GridControlData) object);
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
    public Optional<FormControlData> getValue() {
        return Optional.of(gridPresenter.getValue());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return view;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void requestFocus() {
        gridPresenter.requestFocus();
    }
}

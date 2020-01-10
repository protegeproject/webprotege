package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class NonRepeatingEditor implements ValueEditor<List<FormControlData>> {

    private final ValueEditor<FormControlData> delegate;

    private final HandlerManager handlerManager = new HandlerManager(this);

    public NonRepeatingEditor(ValueEditor<FormControlData> delegate) {
        this.delegate = delegate;
        delegate.asWidget().setWidth("100%");
        delegate.addValueChangeHandler(event -> ValueChangeEvent.fire(this, getValue()));
    }

    @Override
    public void setValue(List<FormControlData> object) {
        if(object.isEmpty()) {
            delegate.clearValue();
        }
        else {
            delegate.setValue(object.get(0));
        }
    }

    @Override
    public void clearValue() {
        delegate.clearValue();
    }

    @Override
    public Optional<List<FormControlData>> getValue() {
        return delegate.getValue().map(Collections::singletonList);
    }

    @Override
    public boolean isDirty() {
        return delegate.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return delegate.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<List<FormControlData>>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return delegate.isWellFormed();
    }

    @Override
    public Widget asWidget() {
        return delegate.asWidget();
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class RepeatingFormControlStackBackingEditor implements FormControlStackBackingEditor {

    private ValueListEditor<FormControlData> delegate;

    private HandlerManager handlerManager;

    public RepeatingFormControlStackBackingEditor(ValueListEditor<FormControlData> delegate) {
        this.delegate = delegate;
        this.handlerManager = new HandlerManager(this);
        this.delegate.addValueChangeHandler(event -> {
           ValueChangeEvent.fire(this, getValue());
        });
        delegate.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
    }

    @Override
    public void setValue(List<FormControlData> object) {
        delegate.setValue(object);
    }

    @Override
    public void clearValue() {
        delegate.clearValue();
    }

    @Override
    public Optional<List<FormControlData>> getValue() {
        return delegate.getValue();
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<List<FormControlData>>> handler) {
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

    @Override
    public void requestFocus() {
        delegate.forEachEditor(ed -> {
            if(ed instanceof HasRequestFocus) {
                ((HasRequestFocus) ed).requestFocus();
            }
        });
    }

    @Override
    public void forEachFormControl(Consumer<FormControl> consumer) {
        delegate.forEachEditor(ed -> consumer.accept((FormControl) ed));
    }

    @Override
    public void setEnabled(boolean editable) {
        delegate.setEnabled(editable);
    }
}

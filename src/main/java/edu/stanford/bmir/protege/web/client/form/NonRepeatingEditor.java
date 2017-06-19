package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class NonRepeatingEditor implements ValueEditor<FormDataList> {

    private final ValueEditor<FormDataValue> delegate;

    public NonRepeatingEditor(ValueEditor<FormDataValue> delegate) {
        this.delegate = delegate;
        delegate.asWidget().setWidth("100%");
    }

    @Override
    public void setValue(FormDataList object) {
        Optional<FormDataValue> first = object.getFirst();
        if(first.isPresent()) {
            delegate.setValue(first.get());
        }
        else {
            delegate.clearValue();
        }
    }

    @Override
    public void clearValue() {
        delegate.clearValue();
    }

    @Override
    public Optional<FormDataList> getValue() {
        Optional<FormDataValue> value = delegate.getValue();
        if(!value.isPresent()) {
            return Optional.of(FormDataList.empty());
        }
        FormDataList delegateValue = new FormDataList(value.get());
        return Optional.of(delegateValue);
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
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<FormDataList>> handler) {
        return new HandlerRegistration() {
            @Override
            public void removeHandler() {

            }
        };
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        delegate.fireEvent(event);
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

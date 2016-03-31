package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormDataTuple;

import java.util.Arrays;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class NonRepeatingEditor implements ValueEditor<List<FormDataTuple>> {

    private final ValueEditor<FormDataTuple> delegate;

    public NonRepeatingEditor(ValueEditor<FormDataTuple> delegate) {
        this.delegate = delegate;
        delegate.asWidget().setWidth("100%");
    }

    @Override
    public void setValue(List<FormDataTuple> object) {
        if(object.isEmpty()) {
            delegate.clearValue();
            return;
        }
        delegate.setValue(object.get(0));
    }

    @Override
    public void clearValue() {
        delegate.clearValue();
    }

    @Override
    public Optional<List<FormDataTuple>> getValue() {
        Optional<FormDataTuple> delegateValue = delegate.getValue();
        if(!delegateValue.isPresent()) {
            return Optional.absent();
        }
        else {
            return Optional.of(Arrays.asList(delegateValue.get()));
        }
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
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<List<FormDataTuple>>> handler) {
        return delegate.addValueChangeHandler(new ValueChangeHandler<Optional<FormDataTuple>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<FormDataTuple>> event) {
                ValueChangeEvent.fire(NonRepeatingEditor.this, getValue());
            }
        });
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

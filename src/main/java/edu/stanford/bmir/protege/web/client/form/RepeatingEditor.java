package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class RepeatingEditor implements ValueEditor<FormDataList> {

    private ValueListEditor<FormDataValue> delegate;

    public RepeatingEditor(ValueListEditor<FormDataValue> delegate) {
        this.delegate = delegate;
        delegate.setEnabled(true);
    }

    @Override
    public void setValue(FormDataList object) {
        delegate.setValue(object.getList());
    }

    @Override
    public void clearValue() {
        delegate.clearValue();
    }

    @Override
    public Optional<FormDataList> getValue() {
        Optional<List<FormDataValue>> value = delegate.getValue();
        if(!value.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new FormDataList(value.get()));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataList>> handler) {
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

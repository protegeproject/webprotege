package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class RepeatingInlineEditor implements ValueEditor<FormDataList> {

    private final ValueListFlexEditorImpl delegate;

    public RepeatingInlineEditor(ValueListFlexEditorImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setValue(FormDataList object) {

    }

    @Override
    public void clearValue() {

    }

    @Override
    public Optional<FormDataList> getValue() {
        return null;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataList>> valueChangeHandler) {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent) {

    }

    @Override
    public boolean isWellFormed() {
        return false;
    }

    @Override
    public Widget asWidget() {
        return null;
    }
}

package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.common.EditingCancelledEvent;
import edu.stanford.bmir.protege.web.client.ui.library.common.EditingCancelledHandler;
import edu.stanford.bmir.protege.web.client.ui.library.common.EditingFinishedEvent;
import edu.stanford.bmir.protege.web.client.ui.library.common.EditingFinishedHandler;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class ReplyEditorPanel extends Composite implements ReplyDisplay {

    interface ReplyEditorPanelUiBinder extends UiBinder<HTMLPanel, ReplyEditorPanel> {

    }

    private static ReplyEditorPanelUiBinder ourUiBinder = GWT.create(ReplyEditorPanelUiBinder.class);

    @UiField
    protected ExpandingTextBox bodyField;

    @UiField
    protected Button saveButton;

    @UiField
    protected Button cancelButton;


    private boolean dirty;


    public ReplyEditorPanel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }


    @UiHandler("saveButton")
    protected void handleSave(ClickEvent clickEvent) {
        fireEvent(new EditingFinishedEvent<String>(getBody()));
    }

    @UiHandler("cancelButton")
    protected void handleCancel(ClickEvent clickEvent) {
        fireEvent(new EditingCancelledEvent());
    }

    @UiHandler("bodyField")
    protected void handleBodyChanged(ValueChangeEvent<String> event) {
        dirty = true;
    }


    @Override
    public String getBody() {
        return bodyField.getText().trim();
    }

    @Override
    public void setValue(NoteContent object) {
        bodyField.setText(object.getBody());
        dirty = false;
    }

    @Override
    public void clearValue() {
        bodyField.setText("");
        dirty = false;
    }

    @Override
    public Optional<NoteContent> getValue() {
        return Optional.of(new NoteContent(getBody()));
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public Widget getWidget() {
        return super.getWidget();
    }

    @Override
    public HandlerRegistration addEditingCancelledHandler(EditingCancelledHandler handler) {
        return this.addHandler(handler, EditingCancelledEvent.TYPE);
    }

    @Override
    public HandlerRegistration addEditingFinishedHandler(EditingFinishedHandler<NoteContent> handler) {
        return this.addHandler(handler, EditingFinishedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }


    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<NoteContent>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
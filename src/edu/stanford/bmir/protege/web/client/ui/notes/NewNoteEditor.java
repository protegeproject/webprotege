package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteField;
import edu.stanford.bmir.protege.web.shared.notes.NoteHeader;
import edu.stanford.bmir.protege.web.shared.notes.NoteType;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class NewNoteEditor extends Composite implements NewNoteDisplay {

    private boolean dirty = false;

    interface NewNoteEditorUiBinder extends UiBinder<HTMLPanel, NewNoteEditor> {

    }

    private static NewNoteEditorUiBinder ourUiBinder = GWT.create(NewNoteEditorUiBinder.class);

    @UiField
    protected TextBox subjectField;

    @UiField
    protected ExpandingTextBox bodyField;

    public NewNoteEditor(UserId userId) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        NoteContent noteContent = NoteContent.builder().addField(NoteField.TYPE, NoteType.getComment()).addField(NoteField.BODY, getBody()).build();
    }

    public String getBody() {
        return bodyField.getText().trim();
    }


    @UiHandler("bodyField")
    protected void handleBodyChange(ValueChangeEvent<String> event) {
        setDirty(true, EventStrategy.FIRE_EVENTS);
    }



    @Override
    public void setValue(NoteContent object) {
        subjectField.setValue(object.getFieldValue(NoteField.SUBJECT).or(""));
        bodyField.setValue(object.getFieldValue(NoteField.BODY).or(""));
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
    }

    @Override
    public void clearValue() {
        subjectField.setText("");
        bodyField.setText("");
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
    }

    @Override
    public Optional<NoteContent> getValue() {
        final NoteContent content = NoteContent.builder().addField(NoteField.SUBJECT, getSubject()).addField(NoteField.BODY, getBody()).build();
        return Optional.of(content);
    }

    private String getSubject() {
        return subjectField.getText().trim();
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    private void setDirty(boolean dirty, EventStrategy fireEvents) {

    }

    @Override
    public boolean isWellFormed() {
        return true;
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
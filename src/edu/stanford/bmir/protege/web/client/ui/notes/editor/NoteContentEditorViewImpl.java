package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteField;
import edu.stanford.bmir.protege.web.shared.notes.NoteType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class NoteContentEditorViewImpl extends Composite implements NoteContentEditorView {

    interface NoteContentEditorViewImplUiBinder extends UiBinder<HTMLPanel, NoteContentEditorViewImpl> {

    }

    private static NoteContentEditorViewImplUiBinder ourUiBinder = GWT.create(NoteContentEditorViewImplUiBinder.class);

    @UiField
    protected TextBoxBase topicField;

    @UiField
    protected TextBoxBase bodyArea;


    private boolean dirty;

    public NoteContentEditorViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setMode(NoteContentEditorMode mode) {
        if (mode == NoteContentEditorMode.NEW_TOPIC) {
            setBodyPlaceholder("Enter comment");
            topicField.setVisible(true);
//            topicField.setEnabled(true);
        }
        else {
            setBodyPlaceholder("Enter reply");
            topicField.setVisible(false);
//            topicField.setEnabled(false);
        }
    }

    private void setBodyPlaceholder(String placeholder) {
        if (bodyArea instanceof HasPlaceholder) {
            ((HasPlaceholder) bodyArea).setPlaceholder(placeholder);
        }
    }


    @Override
    public void setValue(NoteContent object) {
        topicField.setText(object.getSubject().or(""));
        bodyArea.setText(object.getBody().or(""));
        dirty = false;
    }

    @Override
    public void clearValue() {
        topicField.setText("");
        bodyArea.setText("");
        dirty = false;
    }

    @Override
    public Optional<NoteContent> getValue() {
        if (getBody().isEmpty()) {
            return Optional.absent();
        }
        NoteContent.Builder contentBuilder = NoteContent.builder();
        if (!getTopic().isEmpty()) {
            contentBuilder.setSubject(getTopic());
        }
        contentBuilder.setBody(getBody());
        contentBuilder.setNoteType(NoteType.COMMENT);
        return Optional.of(contentBuilder.build());
    }

    private String getBody() {
        return bodyArea.getText().trim();
    }

    private String getTopic() {
        return topicField.getText().trim();
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<NoteContent>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }

    @Override
    public Focusable getInitialFocusable() {
        return bodyArea;
    }
}
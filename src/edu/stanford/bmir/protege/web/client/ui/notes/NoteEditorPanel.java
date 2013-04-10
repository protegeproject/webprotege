package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.common.EditingCancelledEvent;
import edu.stanford.bmir.protege.web.client.ui.library.common.EditingFinishedEvent;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.shared.HasActive;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

import java.util.Date;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteEditorPanel extends Composite implements HasActive {

    interface NoteEditorPanelUiBinder extends UiBinder<HTMLPanel, NoteEditorPanel> {

    }

    private static NoteEditorPanelUiBinder ourUiBinder = GWT.create(NoteEditorPanelUiBinder.class);

    @UiField
    protected Label author;

    @UiField
    protected Label date;

    @UiField
    protected Label body;

    @UiField
    protected Anchor replyAnchor;

    @UiField
    protected ReplyDisplay replyEditor;

    public NoteEditorPanel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public void setAuthor(String authorName) {
        this.author.setText(authorName);
    }

    public void setDate(long timestamp) {
        this.date.setText(new Date(timestamp).toString());
    }

    public void setBody(String body) {
        this.body.setText(body);
    }

    public String getBody() {
        return this.body.getText().trim();
    }



    @UiHandler("replyAnchor")
    protected void handleReplyRequest(ClickEvent event) {
        replyEditor.setVisible(true);
    }

    @UiHandler("replyEditor")
    protected void handleReplyEditingCancelled(EditingCancelledEvent event) {
        replyEditor.setVisible(false);
        replyEditor.clearValue();
    }

    @UiHandler("replyEditor")
    protected void handleReplyEditingFinished(EditingFinishedEvent<NoteContent> event) {
        replyEditor.setVisible(false);
        replyEditor.getBody();
        replyEditor.clearValue();
        GWT.log("EDITED VALUE: " + event.getValue());
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
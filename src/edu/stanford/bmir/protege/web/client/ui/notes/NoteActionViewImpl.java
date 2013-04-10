package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class NoteActionViewImpl extends Composite implements NoteActionView {

    interface NoteActionBarUiBinder extends UiBinder<HTMLPanel, NoteActionViewImpl> {

    }

    private static NoteActionBarUiBinder ourUiBinder = GWT.create(NoteActionBarUiBinder.class);


    private ReplyToNoteHandler replyToNoteHandler = new ReplyToNoteHandler() {
        @Override
        public void handleReplyToNote() {
            GWT.log("No ReplyToNoteHandler registered");
        }
    };

    @UiField
    protected HasClickHandlers replyWidget;


    @UiHandler("replyWidget")
    protected void handleReplyClicked(ClickEvent clickEvent) {
        replyToNoteHandler.handleReplyToNote();
    }

    public NoteActionViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setReplyToNoteHandler(ReplyToNoteHandler handler) {
        replyToNoteHandler = handler;
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}
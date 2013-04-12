package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ButtonBase;
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

    private DeleteNoteHandler deleteNoteHandler = new DeleteNoteHandler() {
        @Override
        public void handleDeleteNote() {
            GWT.log("No DeleteNoteHandler registered");
        }
    };

    @UiField
    protected ButtonBase replyWidget;

    @UiField
    protected ButtonBase deleteWidget;


    @UiHandler("replyWidget")
    protected void handleReplyClicked(ClickEvent clickEvent) {
        replyToNoteHandler.handleReplyToNote();
    }

    @UiHandler("deleteWidget")
    protected void handleDeleteClicked(ClickEvent clickEvent) {
        deleteNoteHandler.handleDeleteNote();
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
    public void setDeleteNoteHandler(DeleteNoteHandler handler) {
        deleteNoteHandler = handler;
    }

    @Override
    public void setCanDelete(boolean canDelete) {
        deleteWidget.setVisible(canDelete);
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}
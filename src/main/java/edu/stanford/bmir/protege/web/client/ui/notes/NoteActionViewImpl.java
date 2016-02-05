package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

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
        public void handleReplyToNote(NoteId noteId, OWLEntity targetEntity) {
        }
    };

    private DeleteNoteHandler deleteNoteHandler = new DeleteNoteHandler() {
        @Override
        public void handleDeleteNote(NoteId noteId) {
        }
    };

    @UiField
    protected ButtonBase replyWidget;

    @UiField
    protected ButtonBase deleteWidget;

    @UiHandler("replyWidget")
    protected void handleReplyClicked(ClickEvent clickEvent) {
        replyToNoteHandler.handleReplyToNote(noteId, selectionModel.getSelection().get().getEntity());
    }

    @UiHandler("deleteWidget")
    protected void handleDeleteClicked(ClickEvent clickEvent) {
        deleteNoteHandler.handleDeleteNote(noteId);
    }

    private NoteId noteId;

    private SelectionModel selectionModel;

    @Inject
    public NoteActionViewImpl(SelectionModel selectionModel) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.selectionModel = selectionModel;
    }

    @Override
    public void setNoteId(NoteId noteId) {
        this.noteId = noteId;
    }

    @Override
    public void setReplyToNoteHandler(ReplyToNoteHandler handler) {
        replyToNoteHandler = handler;
    }

    @Override
    public void setCanReply(boolean canReply) {
        replyWidget.setEnabled(canReply);
    }

    @Override
    public void setDeleteNoteHandler(DeleteNoteHandler handler) {
        deleteNoteHandler = handler;
    }

    @Override
    public void setCanDelete(boolean canDelete) {
        deleteWidget.setEnabled(canDelete);
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}
package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteEditorDialogController;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class ReplyToNoteHandlerImpl implements ReplyToNoteHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final ActiveProjectManager activeProjectManager;

    @Inject
    public ReplyToNoteHandlerImpl(DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
    }

    @Override
    public void handleReplyToNote(final NoteId noteId) {
        NoteEditorDialogController controller = new NoteEditorDialogController(new NoteContentEditorHandler() {
            @Override
            public void handleAccept(Optional<NoteContent> noteContent) {
                if (noteContent.isPresent()) {
                    doReply(noteId, noteContent.get());
                }
            }
        });
        controller.setMode(NoteContentEditorMode.REPLY);
        WebProtegeDialog.showDialog(controller);
    }

    private void doReply(NoteId noteId, NoteContent content) {
        ProjectId projectId = activeProjectManager.getActiveProjectId().get();
        dispatchServiceManager.execute(new AddReplyToNoteAction(projectId, noteId, content), new DispatchServiceCallback<AddReplyToNoteResult>() {
            @Override
            public void handleSuccess(AddReplyToNoteResult result) {
                GWT.log("Replied o.k. " + result);
            }
        });
    }



}

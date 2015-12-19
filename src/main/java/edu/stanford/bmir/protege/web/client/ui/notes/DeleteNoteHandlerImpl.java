package edu.stanford.bmir.protege.web.client.ui.notes;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class DeleteNoteHandlerImpl implements DeleteNoteHandler {


    private final DispatchServiceManager dispatchServiceManager;

    private NoteId noteId;

    public DeleteNoteHandlerImpl(DispatchServiceManager dispatchServiceManager, NoteId noteId) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.noteId = noteId;
    }

    @Override
    public void handleDeleteNote() {
        ProjectId projectId = Application.get().getActiveProject().get();
        dispatchServiceManager.execute(new DeleteNoteAction(projectId, noteId), new DispatchServiceCallback<DeleteNoteResult>() {
            @Override
            public void handleSuccess(DeleteNoteResult result) {

            }
        });
    }
}

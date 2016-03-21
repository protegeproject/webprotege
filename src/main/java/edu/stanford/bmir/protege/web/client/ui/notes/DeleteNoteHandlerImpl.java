package edu.stanford.bmir.protege.web.client.ui.notes;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class DeleteNoteHandlerImpl implements DeleteNoteHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final ActiveProjectManager activeProjectManager;

    @Inject
    public DeleteNoteHandlerImpl(DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
    }

    @Override
    public void handleDeleteNote(OWLEntity targetEntity, NoteId noteId) {
        ProjectId projectId = activeProjectManager.getActiveProjectId().get();
        dispatchServiceManager.execute(new DeleteNoteAction(projectId, targetEntity, noteId), new DispatchServiceCallback<DeleteNoteResult>() {
            @Override
            public void handleSuccess(DeleteNoteResult result) {
            }
        });
    }
}

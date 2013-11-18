package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class DeleteNoteAction extends AbstractHasProjectAction<DeleteNoteResult> {

    private NoteId noteId;

    /**
     * For serialization only!
     */
    private DeleteNoteAction() {
    }

    public DeleteNoteAction(ProjectId projectId, NoteId noteId) {
        super(projectId);
        this.noteId = checkNotNull(noteId);
    }

    public NoteId getNoteId() {
        return noteId;
    }
}

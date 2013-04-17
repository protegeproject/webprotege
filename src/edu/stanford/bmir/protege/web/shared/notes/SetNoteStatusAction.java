package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class SetNoteStatusAction extends AbstractHasProjectAction<SetNoteStatusResult> {

    private NoteId noteId;

    private NoteStatus noteStatus;

    /**
     * For serialization purposes only
     */
    private SetNoteStatusAction() {
    }

    public SetNoteStatusAction(ProjectId projectId, NoteId noteId, NoteStatus noteStatus) {
        super(projectId);
        this.noteId = noteId;
        this.noteStatus = noteStatus;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public NoteStatus getNoteStatus() {
        return noteStatus;
    }
}

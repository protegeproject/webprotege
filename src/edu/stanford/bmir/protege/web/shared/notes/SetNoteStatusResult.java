package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class SetNoteStatusResult extends AbstractHasEventListResult<ProjectEvent<?>> implements HasProjectId {

    private ProjectId projectId;

    private NoteStatus noteStatus;

    /**
     * For serialization purposes only
     */
    private SetNoteStatusResult() {
    }

    public SetNoteStatusResult(EventList<ProjectEvent<?>> eventEventList, ProjectId projectId, NoteId noteId, NoteStatus noteStatus) {
        super(eventEventList);
        this.projectId = projectId;
        this.noteStatus = noteStatus;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public NoteStatus getNoteStatus() {
        return noteStatus;
    }
}

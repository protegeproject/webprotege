package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class SetNoteStatusResult implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventEventList;

    private ProjectId projectId;

    private NoteId noteId;

    private NoteStatus noteStatus;


    private SetNoteStatusResult() {
    }

    public SetNoteStatusResult(EventList<ProjectEvent<?>> eventEventList, ProjectId projectId, NoteId noteId, NoteStatus noteStatus) {
        this.eventEventList = eventEventList;
        this.projectId = projectId;
        this.noteId = noteId;
        this.noteStatus = noteStatus;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventEventList;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public NoteStatus getNoteStatus() {
        return noteStatus;
    }
}

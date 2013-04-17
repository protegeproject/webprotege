package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class AddNoteToEntityAction extends AbstractHasProjectAction<AddNoteToEntityResult> {

    private OWLEntity entity;

    private NoteContent noteContent;

    private AddNoteToEntityAction() {
    }

    public AddNoteToEntityAction(ProjectId projectId, OWLEntity entity, NoteContent noteContent) {
        super(projectId);
        this.entity = entity;
        this.noteContent = noteContent;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public NoteContent getNoteContent() {
        return noteContent;
    }
}

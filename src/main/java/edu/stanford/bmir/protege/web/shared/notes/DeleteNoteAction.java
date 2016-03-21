package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class DeleteNoteAction extends AbstractHasProjectAction<DeleteNoteResult> {

    private NoteId noteId;

    private OWLEntity targetEntity;

    /**
     * For serialization only!
     */
    private DeleteNoteAction() {
    }

    public DeleteNoteAction(ProjectId projectId, OWLEntity targetEntity, NoteId noteId) {
        super(projectId);
        this.targetEntity = checkNotNull(targetEntity);
        this.noteId = checkNotNull(noteId);
    }

    public OWLEntity getTargetEntity() {
        return targetEntity;
    }

    public NoteId getNoteId() {
        return noteId;
    }
}

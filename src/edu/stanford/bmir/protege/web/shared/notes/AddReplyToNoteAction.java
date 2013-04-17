package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class AddReplyToNoteAction extends AbstractHasProjectAction<AddReplyToNoteResult> {

    private NoteId targetNoteId;

    private NoteContent replyContent;

    private AddReplyToNoteAction() {
    }

    public AddReplyToNoteAction(ProjectId projectId, NoteId targetNoteId, NoteContent replyContent) {
        super(projectId);
        this.targetNoteId = checkNotNull(targetNoteId);
        this.replyContent = checkNotNull(replyContent);
    }

    public NoteId getTargetNoteId() {
        return targetNoteId;
    }

    public NoteContent getReplyContent() {
        return replyContent;
    }
}

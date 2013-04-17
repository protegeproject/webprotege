package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class DeleteNoteActionHandler extends AbstractHasProjectActionHandler<DeleteNoteAction, DeleteNoteResult> {

    @Override
    protected RequestValidator<DeleteNoteAction> getAdditionalRequestValidator(DeleteNoteAction action, RequestContext requestContext) {
        return new UserHasProjectWritePermissionValidator<DeleteNoteAction, DeleteNoteResult>();
    }

    @Override
    protected DeleteNoteResult execute(DeleteNoteAction action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag currentEventTag = project.getEventManager().getCurrentTag();
        project.getNotesManager().deleteNoteAndReplies(action.getNoteId());
        return new DeleteNoteResult(action.getNoteId(), project.getEventManager().getEventsFromTag(currentEventTag));
    }

    @Override
    public Class<DeleteNoteAction> getActionClass() {
        return DeleteNoteAction.class;
    }
}

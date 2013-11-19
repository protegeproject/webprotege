package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectCommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteResult;
import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class AddReplyToNoteActionHandler extends AbstractHasProjectActionHandler<AddReplyToNoteAction, AddReplyToNoteResult> {

    private static final UserHasProjectCommentPermissionValidator<AddReplyToNoteAction,AddReplyToNoteResult> VALIDATOR = new UserHasProjectCommentPermissionValidator<AddReplyToNoteAction, AddReplyToNoteResult>();

    @Override
    protected RequestValidator<AddReplyToNoteAction> getAdditionalRequestValidator(AddReplyToNoteAction action, RequestContext requestContext) {
        return VALIDATOR;
    }

    @Override
    protected AddReplyToNoteResult execute(AddReplyToNoteAction action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag startTag = project.getEventManager().getCurrentTag();
        OWLAPINotesManager notesManager = project.getNotesManager();
        Note note = notesManager.addReplyToNote(action.getTargetNoteId(), action.getReplyContent(), executionContext.getUserId());
        return new AddReplyToNoteResult(note, project.getEventManager().getEventsFromTag(startTag));
    }

    @Override
    public Class<AddReplyToNoteAction> getActionClass() {
        return AddReplyToNoteAction.class;
    }
}

package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectCommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityResult;
import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class AddNoteToEntityActionHandler extends AbstractHasProjectActionHandler<AddNoteToEntityAction, AddNoteToEntityResult> {

    @Override
    protected RequestValidator<AddNoteToEntityAction> getAdditionalRequestValidator(AddNoteToEntityAction action, RequestContext requestContext) {
        return new UserHasProjectCommentPermissionValidator<AddNoteToEntityAction, AddNoteToEntityResult>();
    }

    @Override
    protected AddNoteToEntityResult execute(AddNoteToEntityAction action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag tag = project.getEventManager().getCurrentTag();
        Note note = project.getNotesManager().addNoteToEntity(action.getEntity(), action.getNoteContent(), executionContext.getUserId());
        return new AddNoteToEntityResult(project.getEventManager().getEventsFromTag(tag), note);
    }

    @Override
    public Class<AddNoteToEntityAction> getActionClass() {
        return AddNoteToEntityAction.class;
    }
}

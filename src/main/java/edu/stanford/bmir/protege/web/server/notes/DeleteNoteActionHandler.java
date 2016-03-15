package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.dispatch.validators.WritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.DeleteNoteResult;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class DeleteNoteActionHandler extends AbstractHasProjectActionHandler<DeleteNoteAction, DeleteNoteResult> {

    private final ValidatorFactory<WritePermissionValidator> validatorFactory;

    @Inject
    public DeleteNoteActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<WritePermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(final DeleteNoteAction action, final RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
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

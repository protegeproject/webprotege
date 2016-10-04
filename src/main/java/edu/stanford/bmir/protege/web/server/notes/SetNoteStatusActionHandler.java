package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusAction;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusResult;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class SetNoteStatusActionHandler extends AbstractHasProjectActionHandler<SetNoteStatusAction, SetNoteStatusResult> {

    private final ValidatorFactory<CommentPermissionValidator> validatorFactory;

    @Inject
    public SetNoteStatusActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<CommentPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetNoteStatusAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected SetNoteStatusResult execute(SetNoteStatusAction action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag tag = project.getEventManager().getCurrentTag();
        project.getNotesManager().setNoteStatus(action.getNoteId(), action.getNoteStatus());
        EventList<ProjectEvent<?>> eventList = project.getEventManager().getEventsFromTag(tag);
        return new SetNoteStatusResult(eventList, project.getProjectId(), action.getNoteId(), action.getNoteStatus());
    }

    @Override
    public Class<SetNoteStatusAction> getActionClass() {
        return SetNoteStatusAction.class;
    }
}

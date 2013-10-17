package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusAction;
import edu.stanford.bmir.protege.web.shared.notes.SetNoteStatusResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class SetNoteStatusActionHandler extends AbstractHasProjectActionHandler<SetNoteStatusAction, SetNoteStatusResult> {

    @Override
    protected RequestValidator<SetNoteStatusAction> getAdditionalRequestValidator(SetNoteStatusAction action, RequestContext requestContext) {
        return new UserHasProjectWritePermissionValidator<SetNoteStatusAction, SetNoteStatusResult>();
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

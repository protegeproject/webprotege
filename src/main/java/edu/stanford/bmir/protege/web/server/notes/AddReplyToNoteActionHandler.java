package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteAction;
import edu.stanford.bmir.protege.web.shared.notes.AddReplyToNoteResult;
import edu.stanford.bmir.protege.web.shared.notes.Note;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class AddReplyToNoteActionHandler extends AbstractHasProjectActionHandler<AddReplyToNoteAction, AddReplyToNoteResult> {


    private final ValidatorFactory<CommentPermissionValidator> validatorFactory;

    @Inject
    public AddReplyToNoteActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<CommentPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(AddReplyToNoteAction action, RequestContext requestContext) {

        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected AddReplyToNoteResult execute(AddReplyToNoteAction action, OWLAPIProject project, ExecutionContext executionContext) {
        EventTag startTag = project.getEventManager().getCurrentTag();
        OWLAPINotesManager notesManager = project.getNotesManager();
        Note note = notesManager.addReplyToNote(action.getRootEntity(), action.getTargetNoteId(), action.getReplyContent(), executionContext.getUserId());
        return new AddReplyToNoteResult(note, project.getEventManager().getEventsFromTag(startTag));
    }

    @Override
    public Class<AddReplyToNoteAction> getActionClass() {
        return AddReplyToNoteAction.class;
    }
}

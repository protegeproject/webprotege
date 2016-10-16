package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.CommentPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.issues.*;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentActionHandler extends AbstractHasProjectActionHandler<EditCommentAction, EditCommentResult> {

    private final ValidatorFactory<CommentPermissionValidator> validator;

    private final EntityDiscussionThreadRepository repository;

    @Inject
    public EditCommentActionHandler(OWLAPIProjectManager projectManager,
                                    ValidatorFactory<CommentPermissionValidator> validator,
                                    EntityDiscussionThreadRepository repository) {
        super(projectManager);
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public Class<EditCommentAction> getActionClass() {
        return EditCommentAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(EditCommentAction action, RequestContext requestContext) {
        return validator.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected EditCommentResult execute(EditCommentAction action,
                                        OWLAPIProject project,
                                        ExecutionContext executionContext) {
        EventTag fromTag = project.getEventManager().getCurrentTag();

        Optional<EntityDiscussionThread> thread = repository.getThread(action.getThreadId());
        if (!thread.isPresent()) {
            throw new RuntimeException("Invalid comment thread");
        }
        EntityDiscussionThread t = thread.get();
        String renderedComment = new CommentRenderer().renderComment(action.getBody());
        Optional<Comment> updatedComment = t.getComments().stream()
                                            .filter(c -> c.getId().equals(action.getCommentId()))
                                            .limit(1)
                                            .map(c -> new Comment(c.getId(),
                                                                  c.getCreatedBy(),
                                                                  c.getCreatedAt(),
                                                                  Optional.of(System.currentTimeMillis()),
                                                                  action.getBody(),
                                                                  renderedComment))
                                            .peek(c -> repository.updateComment(t.getId(), c))
                                            .findFirst();
        updatedComment.ifPresent(comment -> project.getEventManager().postEvent(new CommentUpdatedEvent(action.getProjectId(), t.getId(), comment)));
        EventList<ProjectEvent<?>> eventList = project.getEventManager().getEventsFromTag(fromTag);
        return new EditCommentResult(updatedComment, eventList);
    }

}

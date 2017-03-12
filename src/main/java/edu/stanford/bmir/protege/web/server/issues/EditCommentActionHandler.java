package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.issues.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_OWN_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentActionHandler extends AbstractHasProjectActionHandler<EditCommentAction, EditCommentResult> {

    private final EntityDiscussionThreadRepository repository;

    @Inject
    public EditCommentActionHandler(ProjectManager projectManager,
                                    AccessManager accessManager,
                                    EntityDiscussionThreadRepository repository) {
        super(projectManager, accessManager);
        this.repository = repository;
    }

    @Override
    public Class<EditCommentAction> getActionClass() {
        return EditCommentAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_OWN_OBJECT_COMMENT;
    }

    @Override
    protected EditCommentResult execute(EditCommentAction action,
                                        Project project,
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

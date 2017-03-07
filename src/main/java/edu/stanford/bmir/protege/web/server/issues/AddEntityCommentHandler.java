package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class AddEntityCommentHandler extends AbstractHasProjectActionHandler<AddEntityCommentAction, AddEntityCommentResult> {

    private final EntityDiscussionThreadRepository repository;

    private final MentionedUsersEmailer mentionedUsersEmailer;

    @Inject
    public AddEntityCommentHandler(@Nonnull ProjectManager projectManager,
                                   @Nonnull AccessManager accessManager,
                                   @Nonnull EntityDiscussionThreadRepository repository,
                                   @Nonnull MentionedUsersEmailer mentionedUsersEmailer) {
        super(projectManager, accessManager);
        this.repository = checkNotNull(repository);
        this.mentionedUsersEmailer = checkNotNull(mentionedUsersEmailer);
    }

    @Override
    public Class<AddEntityCommentAction> getActionClass() {
        return AddEntityCommentAction.class;
    }

    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return CREATE_OBJECT_COMMENT;
    }

    @Override
    protected AddEntityCommentResult execute(AddEntityCommentAction action,
                                             Project project,
                                             ExecutionContext executionContext) {
        UserId createdBy = executionContext.getUserId();
        long createdAt = System.currentTimeMillis();
        CommentRenderer r = new CommentRenderer();
        String rawComment = action.getComment();
        String renderedComment = r.renderComment(rawComment);
        Comment comment = new Comment(CommentId.create(),
                                      createdBy,
                                      createdAt,
                                      Optional.empty(),
                                      rawComment,
                                      renderedComment);
        ThreadId threadId = action.getThreadId();
        repository.addCommentToThread(threadId, comment);
        postCommentPostedEvent(project, threadId, comment);

        UserId commentingUser = executionContext.getUserId();
        mentionedUsersEmailer.sendEmailsToMentionedUsers(rawComment, renderedComment, commentingUser);
        return new AddEntityCommentResult(action.getProjectId(), threadId, comment, renderedComment);

    }


    /**
     * Post a {@link CommentPostedEvent} to the project event bus.
     *
     * @param project  The project.
     * @param threadId The thread that the comment was added to.
     * @param comment  The comment that was added.
     */
    private void postCommentPostedEvent(@Nonnull Project project,
                                        @Nonnull ThreadId threadId,
                                        @Nonnull Comment comment) {
        Optional<EntityDiscussionThread> thread = repository.getThread(threadId);
        thread.ifPresent(t -> {
            OWLEntityData entityData = project.getRenderingManager().getRendering(t.getEntity());
            int commentCount = repository.getCommentsCount(project.getProjectId(), t.getEntity());
            CommentPostedEvent event = new CommentPostedEvent(project.getProjectId(),
                                                              threadId,
                                                              comment,
                                                              Optional.of(entityData),
                                                              commentCount);
            project.getEventManager().postEvent(event);

        });
    }

}

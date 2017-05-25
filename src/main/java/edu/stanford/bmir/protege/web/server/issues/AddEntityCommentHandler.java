package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.webhook.CommentPostedSlackWebhookInvoker;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
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

    private final CommentNotificationEmailer notificationsEmailer;

    private final CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker;

    private final ProjectDetailsRepository projectDetailsRepository;

    @Inject
    public AddEntityCommentHandler(@Nonnull ProjectManager projectManager,
                                   @Nonnull AccessManager accessManager,
                                   @Nonnull EntityDiscussionThreadRepository repository,
                                   @Nonnull CommentNotificationEmailer notificationsEmailer,
                                   @Nonnull CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker,
                                   @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        super(projectManager, accessManager);
        this.repository = checkNotNull(repository);
        this.notificationsEmailer = checkNotNull(notificationsEmailer);
        this.commentPostedSlackWebhookInvoker = checkNotNull(commentPostedSlackWebhookInvoker);
        this.projectDetailsRepository = checkNotNull(projectDetailsRepository);
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
    protected AddEntityCommentResult execute(@Nonnull AddEntityCommentAction action,
                                             @Nonnull Project project,
                                             @Nonnull ExecutionContext executionContext) {
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
        repository.getThread(threadId);
        repository.getThread(threadId).ifPresent(thread -> {
            notificationsEmailer.sendCommentPostedNotification(project.getProjectId(),
                                                               thread,
                                                               comment);
            commentPostedSlackWebhookInvoker.invoke(project.getProjectId(),
                                                    projectDetailsRepository.findOne(project.getProjectId()).map(
                                                            ProjectDetails::getDisplayName).orElse("Project"),
                                                    project.getRenderingManager().getRendering(thread.getEntity()),
                                                    comment);
        });

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
            int openCommentCount = repository.getOpenCommentsCount(project.getProjectId(), t.getEntity());
            CommentPostedEvent event = new CommentPostedEvent(project.getProjectId(),
                                                              threadId,
                                                              comment,
                                                              Optional.of(entityData),
                                                              commentCount,
                                                              openCommentCount);
            project.getEventManager().postEvent(event);

        });
    }

}

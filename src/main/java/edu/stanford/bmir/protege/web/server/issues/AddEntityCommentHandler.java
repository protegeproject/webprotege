package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.server.webhook.CommentPostedSlackWebhookInvoker;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
public class AddEntityCommentHandler implements ProjectActionHandler<AddEntityCommentAction, AddEntityCommentResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HasGetRendering renderer;

    @Nonnull
    private final HasPostEvents<ProjectEvent<?>> eventManager;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final CommentNotificationEmailer notificationsEmailer;

    @Nonnull
    private final CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public AddEntityCommentHandler(@Nonnull ProjectId projectId,
                                   @Nonnull HasGetRendering renderer,
                                   @Nonnull HasPostEvents<ProjectEvent<?>> eventManager,
                                   @Nonnull EntityDiscussionThreadRepository repository,
                                   @Nonnull CommentNotificationEmailer notificationsEmailer,
                                   @Nonnull CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker,
                                   @Nonnull ProjectDetailsRepository projectDetailsRepository,
                                   @Nonnull AccessManager accessManager) {
        this.projectId = projectId;
        this.renderer = renderer;
        this.eventManager = eventManager;
        this.repository = repository;
        this.notificationsEmailer = notificationsEmailer;
        this.commentPostedSlackWebhookInvoker = commentPostedSlackWebhookInvoker;
        this.projectDetailsRepository = projectDetailsRepository;
        this.accessManager = accessManager;
    }

    @Override
    public Class<AddEntityCommentAction> getActionClass() {
        return AddEntityCommentAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(AddEntityCommentAction action, RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager,
                                              projectId,
                                              requestContext.getUserId(),
                                              CREATE_OBJECT_COMMENT.getActionId());

    }

    @Override
    public AddEntityCommentResult execute(@Nonnull AddEntityCommentAction action,
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
        postCommentPostedEvent(threadId, comment);
        repository.getThread(threadId);
        repository.getThread(threadId).ifPresent(thread -> {
            notificationsEmailer.sendCommentPostedNotification(projectId,
                                                               thread,
                                                               comment);
            commentPostedSlackWebhookInvoker.invoke(projectId,
                                                    projectDetailsRepository.findOne(projectId).map(
                                                            ProjectDetails::getDisplayName).orElse("Project"),
                                                    renderer.getRendering(thread.getEntity()),
                                                    comment);
        });

        return new AddEntityCommentResult(action.getProjectId(), threadId, comment, renderedComment);

    }


    /**
     * Post a {@link CommentPostedEvent} to the project event bus.
     *
     * @param threadId The thread that the comment was added to.
     * @param comment  The comment that was added.
     */
    private void postCommentPostedEvent(@Nonnull ThreadId threadId,
                                        @Nonnull Comment comment) {
        Optional<EntityDiscussionThread> thread = repository.getThread(threadId);
        thread.ifPresent(t -> {
            OWLEntityData entityData = renderer.getRendering(t.getEntity());
            int commentCount = repository.getCommentsCount(projectId, t.getEntity());
            int openCommentCount = repository.getOpenCommentsCount(projectId, t.getEntity());
            CommentPostedEvent event = new CommentPostedEvent(projectId,
                                                              threadId,
                                                              comment,
                                                              Optional.of(entityData),
                                                              commentCount,
                                                              openCommentCount);
            eventManager.postEvent(event);

        });
    }

}

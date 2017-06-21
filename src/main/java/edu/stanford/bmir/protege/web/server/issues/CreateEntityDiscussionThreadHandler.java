package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.server.webhook.CommentPostedSlackWebhookInvoker;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadHandler extends AbstractHasProjectActionHandler<CreateEntityDiscussionThreadAction, CreateEntityDiscussionThreadResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nonnull
    private final CommentNotificationEmailer notificationsEmailer;

    @Nonnull
    private final CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker;

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final HasGetRendering renderer;


    @Inject
    public CreateEntityDiscussionThreadHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull EntityDiscussionThreadRepository repository,
                                               @Nonnull ProjectDetailsRepository projectDetailsRepository,
                                               @Nonnull CommentNotificationEmailer notificationsEmailer,
                                               @Nonnull CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker,
                                               @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                               @Nonnull HasGetRendering renderer) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.projectDetailsRepository = projectDetailsRepository;
        this.notificationsEmailer = notificationsEmailer;
        this.commentPostedSlackWebhookInvoker = commentPostedSlackWebhookInvoker;
        this.eventManager = eventManager;
        this.renderer = renderer;
    }

    @Override
    public Class<CreateEntityDiscussionThreadAction> getActionClass() {
        return CreateEntityDiscussionThreadAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return CREATE_OBJECT_COMMENT;
    }

    @Override
    public CreateEntityDiscussionThreadResult execute(CreateEntityDiscussionThreadAction action,
                                                         ExecutionContext executionContext) {
        String rawComment = action.getComment();
        CommentRenderer commentRenderer = new CommentRenderer();
        String renderedComment = commentRenderer.renderComment(rawComment);
        UserId commentingUser = executionContext.getUserId();
        Comment comment = new Comment(
                CommentId.create(),
                commentingUser,
                System.currentTimeMillis(),
                Optional.empty(),
                rawComment,
                renderedComment);
        OWLEntity entity = action.getEntity();
        EntityDiscussionThread thread = new EntityDiscussionThread(ThreadId.create(),
                                                                   action.getProjectId(),
                                                                   entity,
                                                                   Status.OPEN,
                                                                   ImmutableList.of(comment));
        repository.saveThread(thread);
        eventManager.postEvent(new DiscussionThreadCreatedEvent(thread));
        int commentCount = repository.getCommentsCount(projectId, entity);
        int openCommentCount = repository.getOpenCommentsCount(projectId, entity);
        Optional<OWLEntityData> rendering = Optional.of(renderer.getRendering(entity));
        eventManager.postEvent(new CommentPostedEvent(projectId,
                                                      thread.getId(),
                                                      comment,
                                                      rendering,
                                                      commentCount,
                                                      openCommentCount));
        notificationsEmailer.sendCommentPostedNotification(projectId,
                                                           thread,
                                                           comment);
        commentPostedSlackWebhookInvoker.invoke(projectId,
                                                projectDetailsRepository.findOne(projectId).map(
                                                        ProjectDetails::getDisplayName).orElse("Project"),
                                                renderer.getRendering(thread.getEntity()),
                                                comment);

        List<EntityDiscussionThread> threads = repository.findThreads(projectId, entity);
        return new CreateEntityDiscussionThreadResult(ImmutableList.copyOf(threads));
    }
}

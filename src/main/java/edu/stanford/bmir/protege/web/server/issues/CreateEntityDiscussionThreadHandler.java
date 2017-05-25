package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
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
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadHandler extends AbstractHasProjectActionHandler<CreateEntityDiscussionThreadAction, CreateEntityDiscussionThreadResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nonnull
    private final CommentNotificationEmailer notificationsEmailer;

    @Nonnull
    private final CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker;

    @Inject
    public CreateEntityDiscussionThreadHandler(@Nonnull ProjectManager projectManager,
                                               @Nonnull ProjectDetailsRepository projectDetailsRepository,
                                               @Nonnull AccessManager accessManager,
                                               @Nonnull EntityDiscussionThreadRepository repository,
                                               @Nonnull CommentNotificationEmailer notificationsEmailer,
                                               @Nonnull CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker) {
        super(projectManager, accessManager);
        this.projectDetailsRepository = checkNotNull(projectDetailsRepository);
        this.repository = checkNotNull(repository);
        this.notificationsEmailer = checkNotNull(notificationsEmailer);
        this.commentPostedSlackWebhookInvoker = checkNotNull(commentPostedSlackWebhookInvoker);
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
    protected CreateEntityDiscussionThreadResult execute(CreateEntityDiscussionThreadAction action,
                                                         Project project,
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
        project.getEventManager().postEvent(new DiscussionThreadCreatedEvent(thread));
        ProjectId projectId = project.getProjectId();
        int commentCount = repository.getCommentsCount(projectId, entity);
        int openCommentCount = repository.getOpenCommentsCount(projectId, entity);
        Optional<OWLEntityData> rendering = Optional.of(project.getRenderingManager().getRendering(entity));
        project.getEventManager().postEvent(new CommentPostedEvent(projectId,
                                                                   thread.getId(),
                                                                   comment,
                                                                   rendering,
                                                                   commentCount,
                                                                   openCommentCount));
        notificationsEmailer.sendCommentPostedNotification(projectId,
                                                           thread,
                                                           comment);
        commentPostedSlackWebhookInvoker.invoke(project.getProjectId(),
                                                projectDetailsRepository.findOne(project.getProjectId()).map(
                                                        ProjectDetails::getDisplayName).orElse("Project"),
                                                project.getRenderingManager().getRendering(thread.getEntity()),
                                                comment);

        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), entity);
        return new CreateEntityDiscussionThreadResult(ImmutableList.copyOf(threads));
    }
}

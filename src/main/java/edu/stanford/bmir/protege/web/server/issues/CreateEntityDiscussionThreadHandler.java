package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
    private final MentionedUsersEmailer mentionedUsersEmailer;

    @Inject
    public CreateEntityDiscussionThreadHandler(@Nonnull ProjectManager projectManager,
                                               @Nonnull AccessManager accessManager,
                                               @Nonnull EntityDiscussionThreadRepository repository,
                                               @Nonnull MentionedUsersEmailer mentionedUsersEmailer) {
        super(projectManager, accessManager);
        this.repository = checkNotNull(repository);
        this.mentionedUsersEmailer = checkNotNull(mentionedUsersEmailer);
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
        EntityDiscussionThread thread = new EntityDiscussionThread(ThreadId.create(),
                                                                   action.getProjectId(),
                                                                   action.getEntity(),
                                                                   Status.OPEN,
                                                                   ImmutableList.of(comment));
        repository.saveThread(thread);
        project.getEventManager().postEvent(new DiscussionThreadCreatedEvent(thread));

        mentionedUsersEmailer.sendEmailsToMentionedUsers(rawComment, renderedComment, commentingUser);

        List<EntityDiscussionThread> threads = repository.findThreads(action.getProjectId(), action.getEntity());
        return new CreateEntityDiscussionThreadResult(ImmutableList.copyOf(threads));
    }
}

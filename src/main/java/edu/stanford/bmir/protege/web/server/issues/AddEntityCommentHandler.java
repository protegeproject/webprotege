package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
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

    private final UserDetailsManager userDetailsManager;

    private final SendMail sendMail;

    @Inject
    public AddEntityCommentHandler(@Nonnull ProjectManager projectManager,
                                   @Nonnull AccessManager accessManager,
                                   @Nonnull EntityDiscussionThreadRepository repository,
                                   @Nonnull UserDetailsManager userDetailsManager,
                                   @Nonnull SendMail sendMail) {
        super(projectManager, accessManager);
        this.repository = checkNotNull(repository);
        this.userDetailsManager = checkNotNull(userDetailsManager);
        this.sendMail = checkNotNull(sendMail);
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
        sendEmailsToMentionedUsers(rawComment, renderedComment, commentingUser);
        return new AddEntityCommentResult(action.getProjectId(), threadId, comment, renderedComment);

    }


    /**
     * Post a {@link CommentPostedEvent} to the project event bus.
     * @param project The project.
     * @param threadId The thread that the comment was added to.
     * @param comment The comment that was added.
     */
    private void postCommentPostedEvent(@Nonnull Project project,
                                        @Nonnull ThreadId threadId,
                                        @Nonnull Comment comment) {
        Optional<OWLEntityData> entity = repository.getThread(threadId)
                                                   .map(thread -> thread.getEntity())
                                                   .map(e -> project.getRenderingManager().getRendering(e));
        CommentPostedEvent event = new CommentPostedEvent(project.getProjectId(),
                                                          threadId,
                                                          comment,
                                                          entity);
        project.getEventManager().postEvent(event);
    }

    /**
     * Sets out emails to users that have been mentioned in a posted comment.
     * @param rawComment The raw comment.
     * @param renderedComment The rendered comment.
     * @param commentingUser The user that made the comment.
     */
    private void sendEmailsToMentionedUsers(@Nonnull String rawComment,
                                            @Nonnull String renderedComment,
                                            @Nonnull UserId commentingUser) {
        MentionParser mentionParser = new MentionParser();
        List<ParsedMention> mentionList = mentionParser.parseMentions(rawComment);
        mentionList.forEach(m -> m.getParsedMention().getMentionedUserId().ifPresent(u -> {
            sendEmailToMentionedUser(u, renderedComment, commentingUser);
        }));
    }

    /**
     * Send an email to a user that was mentioned in the comment.
     * @param mentionedUser The user that was mentioned in the comment.  The email will be sent to this user.
     * @param commentRendering The rendering of the comment.
     * @param commentingUser The user that made the comment.
     */
    private void sendEmailToMentionedUser(@Nonnull UserId mentionedUser,
                                          @Nonnull String commentRendering,
                                          @Nonnull UserId commentingUser) {
        userDetailsManager.getUserDetails(mentionedUser).ifPresent(userDetails -> {
            userDetails.getEmailAddress().ifPresent(emailAddress -> {
                String text = formatEmailMessage(commentRendering, commentingUser);
                sendMail.sendMail(emailAddress, commentingUser.getUserName() + " has mentioned you in a comment", text);
            });
        });
    }

    private String formatEmailMessage(String commentRendering, UserId commentingUser) {
        return commentingUser.getUserName() + " has mentioned you in a comment:\n\n"
                            + commentRendering + "\n\n\n";
    }
}

package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.issues.mention.MentionParser;
import edu.stanford.bmir.protege.web.shared.issues.mention.ParsedMention;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 */
class MentionedUsersEmailer {

    private UserDetailsManager userDetailsManager;

    private SendMail sendMail;

    private MentionParser mentionParser;

    @Inject
    public MentionedUsersEmailer(@Nonnull UserDetailsManager userDetailsManager,
                                 @Nonnull SendMail sendMail,
                                 @Nonnull MentionParser mentionParser) {
        this.userDetailsManager = checkNotNull(userDetailsManager);
        this.sendMail = checkNotNull(sendMail);
        this.mentionParser = checkNotNull(mentionParser);
    }

    /**
     * Sets out emails to users that have been mentioned in a comment.
     *
     * @param rawComment      The raw comment.
     * @param renderedComment The rendered comment.
     * @param commentingUser  The user that made the comment.
     */
    public void sendEmailsToMentionedUsers(@Nonnull String rawComment,
                                           @Nonnull String renderedComment,
                                           @Nonnull UserId commentingUser) {
        List<ParsedMention> mentionList = mentionParser.parseMentions(rawComment);
        mentionList.forEach(m -> m.getParsedMention().getMentionedUserId().ifPresent(u -> {
            sendEmailToMentionedUser(u, renderedComment, commentingUser);
        }));
    }

    /**
     * Send an email to a user that was mentioned in the comment.
     *
     * @param mentionedUser    The user that was mentioned in the comment.  The email will be sent to this user.
     * @param commentRendering The rendering of the comment.
     * @param commentingUser   The user that made the comment.
     */
    private void sendEmailToMentionedUser(@Nonnull UserId mentionedUser,
                                          @Nonnull String commentRendering,
                                          @Nonnull UserId commentingUser) {
        userDetailsManager.getUserDetails(mentionedUser).ifPresent(userDetails -> {
            userDetails.getEmailAddress().ifPresent(emailAddress -> {
                String text = formatEmailMessage(commentRendering, commentingUser);
                sendMail.sendMail(emailAddress,
                                  commentingUser.getUserName() + " has mentioned you in a comment",
                                  text);
            });
        });
    }


    private String formatEmailMessage(String commentRendering, UserId commentingUser) {
        return commentingUser.getUserName() + " has mentioned you in a comment:\n\n"
                + commentRendering + "\n\n\n";
    }

}

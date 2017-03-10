package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.inject.ApplicationName;
import edu.stanford.bmir.protege.web.server.mail.SendMail;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2017
 */
public class CommentNotificationsEmailer {

    private final ProjectDetailsManager projectDetailsManager;

    private final UserDetailsManager userDetailsManager;

    private final AccessManager accessManager;

    private final SendMail sendMail;

    private final DiscussionThreadParticipantsExtractor participantsExtractor;

    @ApplicationName
    private final String applicationName;

    @Inject
    public CommentNotificationsEmailer(ProjectDetailsManager projectDetailsManager,
                                       UserDetailsManager userDetailsManager,
                                       AccessManager accessManager,
                                       SendMail sendMail,
                                       DiscussionThreadParticipantsExtractor participantsExtractor,
                                       @ApplicationName String applicationName) {
        this.projectDetailsManager = projectDetailsManager;
        this.userDetailsManager = userDetailsManager;
        this.accessManager = accessManager;
        this.sendMail = sendMail;
        this.participantsExtractor = participantsExtractor;
        this.applicationName = applicationName;
    }

    public void sendCommentPostedNotification(@Nonnull ProjectId projectId,
                                              @Nonnull EntityDiscussionThread thread,
                                              @Nonnull Comment postedComment) {
        Collection<UserDetails> userDetails = getParticipantUserDetails(projectId, thread);
        sendEmailToUsers(userDetails, projectId, thread, postedComment);
    }

    private Collection<UserDetails> getParticipantUserDetails(@Nonnull ProjectId projectId,
                                                              @Nonnull EntityDiscussionThread thread) {
        return getUsersToNotify(projectId, thread).stream()
                .map(userDetailsManager::getUserDetails)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Collection<UserId> getUsersToNotify(@Nonnull ProjectId projectId,
                                              @Nonnull EntityDiscussionThread thread) {
        // Thread participants
        Set<UserId> threadParticipants = participantsExtractor.extractParticipants(thread);
        // Users with specified access
        Set<UserId> projectParticipants = accessManager.getSubjectsWithAccessToResource(new ProjectResource(projectId)).stream()
                .map(Subject::getUserId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        return Sets.union(threadParticipants, projectParticipants);
    }


    private void sendEmailToUsers(@Nonnull Collection<UserDetails> userDetails,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull EntityDiscussionThread discussionThread,
                                 @Nonnull Comment postedComment) {
        List<String> emailAddresses = userDetails.stream()
                .map(UserDetails::getEmailAddress)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        sendMail.sendMail(emailAddresses,
                          formatSubjectLine(projectId, discussionThread, postedComment),
                          formatMessage(projectId, discussionThread, postedComment));
    }



    private String formatSubjectLine(@Nonnull ProjectId projectId,
                                     @Nonnull EntityDiscussionThread thread,
                                     @Nonnull Comment postedComment) {
        return String.format("[%s] Comment posted by %s",
                             projectDetailsManager.getProjectDetails(projectId).getDisplayName(),
                             postedComment.getCreatedBy().getUserName());
    }

    private String formatMessage(@Nonnull ProjectId projectId,
                                 @Nonnull EntityDiscussionThread thread,
                                 @Nonnull Comment postedComment) {
        return String.format("%s posted a comment in %s\n\n" +
                                     "%s\n\n" +
//                                     "<a href=\"%s\">View in %s</a>" +
                                     "You are receiving this notification because you are a participant in the mentioned project.\n",
                             postedComment.getCreatedBy().getUserName(),
                             projectDetailsManager.getProjectDetails(projectId).getDisplayName(),
                             postedComment.getRenderedBody(),
                             thread.getEntity().getIRI(),
                             applicationName
                             );
    }

}

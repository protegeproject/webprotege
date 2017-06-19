package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.shared.issues.CommentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
public class CommentMessageIdGenerator {

    private static final String OBJECT_CATEGORY = "comments";

    private final MessageIdGenerator messageIdGenerator;

    @Inject
    public CommentMessageIdGenerator(MessageIdGenerator messageIdGenerator) {
        this.messageIdGenerator = checkNotNull(messageIdGenerator);
    }

    /**
     * Generates a message id for the specified comment object.
     * @param projectId The project id that identifies the project to which the comment belongs.
     * @param commentId The comment id.
     * @return A message id that can be used in a message-id field about the comment.
     */
    @Nonnull
    public MessageId generateCommentMessageId(@Nonnull ProjectId projectId,
                                           @Nonnull CommentId commentId) {
        return messageIdGenerator.generateProjectMessageId(checkNotNull(projectId),
                                                           OBJECT_CATEGORY,
                                                           checkNotNull(commentId).getId());
    }
}

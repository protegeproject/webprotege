package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class UserIsDicussionThreadCreatorValidator implements RequestValidator {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final ThreadId threadId;

    @Nonnull
    private final UserId userId;

    public UserIsDicussionThreadCreatorValidator(@Nonnull EntityDiscussionThreadRepository repository,
                                                 @Nonnull ThreadId threadId,
                                                 @Nonnull UserId userId) {
        this.repository = checkNotNull(repository);
        this.threadId = checkNotNull(threadId);
        this.userId = checkNotNull(userId);
    }

    @Override
    public RequestValidationResult validateAction() {
        return repository.getThread(threadId)
                         .map(t -> t.isCreatedBy(userId))
                         .map(b -> b ?
                                 RequestValidationResult.getValid() :
                                 RequestValidationResult.getInvalid("Only thread owners can close discussion threads."))
                         .orElse(RequestValidationResult.getInvalid("Invalid request"));
    }
}

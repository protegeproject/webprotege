package edu.stanford.bmir.protege.web.client.user;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/12/15
 */
public interface LoggedInUserProvider {

    @Nonnull
    UserId getCurrentUserId();
}

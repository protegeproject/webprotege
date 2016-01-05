package edu.stanford.bmir.protege.web.client;

import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/12/15
 */
public interface LoggedInUserProvider {

    UserId getCurrentUserId();
}

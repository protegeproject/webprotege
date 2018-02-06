package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public interface HasUserIds {

    Collection<UserId> getUserIds();
}

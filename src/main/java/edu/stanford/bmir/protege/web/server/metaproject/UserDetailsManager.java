package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.user.HasUserIds;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface UserDetailsManager extends HasUserIds {

    @Override
    Collection<UserId> getUserIds();

    User getUser(String userNameOrEmail);

    UserDetails getUserDetails(UserId userId);

    Optional<String> getEmail(UserId userId);

    void setEmail(UserId userId, String email);


}

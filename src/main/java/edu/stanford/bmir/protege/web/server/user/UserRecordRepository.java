package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.repository.CrudRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public interface UserRecordRepository extends CrudRepository<UserRecord, UserId> {

    UserRecord findOneByEmailAddress(String emailAddress);
}

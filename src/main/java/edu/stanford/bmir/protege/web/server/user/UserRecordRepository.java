package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public interface UserRecordRepository extends Repository<UserRecord, UserId> {

    Stream<UserRecord> findAll();

    Optional<UserRecord> findOne(UserId userId);

    Optional<UserRecord> findOneByEmailAddress(String emailAddress);
    
    void save(UserRecord userRecord);

    void delete(UserRecord userRecord);
}

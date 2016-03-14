package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public interface AccessControlListRepository extends Repository<AccessControlListEntry, Long> {

    Stream<AccessControlListEntry> findByProjectId(ProjectId projectId);

    Stream<AccessControlListEntry> findByUserId(UserId userId);

    Stream<AccessControlListEntry> findByProjectIdAndUserId(ProjectId projectId, UserId userId);

    void deleteByProjectId(ProjectId projectId);

    AccessControlListEntry save(AccessControlListEntry entry);

    Iterable<AccessControlListEntry> save(Iterable<AccessControlListEntry> iterable);
}

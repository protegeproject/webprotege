package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public interface AccessControlListRepository extends CrudRepository<AccessControlListEntry, Long> {

    Iterable<AccessControlListEntry> findByProjectId(ProjectId projectId);

    void deleteByProjectId(ProjectId projectId);
}

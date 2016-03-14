package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public interface WorldProjectPermissionRecordRepository extends Repository<WorldProjectPermissionRecord, ProjectId> {

    WorldProjectPermissionRecord save(WorldProjectPermissionRecord s);

    Stream<WorldProjectPermissionRecord> findAllByProjectId(ProjectId projectId);

    void deleteAllByProjectId(ProjectId projectId);
}

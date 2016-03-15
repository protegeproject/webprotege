package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public interface ProjectPermissionRecordRepository extends Repository<ProjectPermissionRecord, Long> {

    Stream<ProjectPermissionRecord> findByProjectId(ProjectId projectId);

    Stream<ProjectPermissionRecord> findByUserId(UserId userId);

    Stream<ProjectPermissionRecord> findByProjectIdAndUserId(ProjectId projectId, UserId userId);

    void deleteByProjectId(ProjectId projectId);

    ProjectPermissionRecord save(ProjectPermissionRecord entry);

    Iterable<ProjectPermissionRecord> save(Iterable<ProjectPermissionRecord> iterable);

    int countByProjectIdAndUserIdAndPermission(ProjectId projectId, UserId userId, Permission permission);
}

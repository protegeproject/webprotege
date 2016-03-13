package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public interface ProjectRecordRepository extends Repository<ProjectRecord, ProjectId> {

    Stream<ProjectRecord> findAll();

    Optional<ProjectRecord> findOne(ProjectId projectId);

    Stream<ProjectRecord> findByOwner(UserId owner);

    void save(ProjectRecord projectRecord);

    void delete(ProjectRecord projectRecord);
}

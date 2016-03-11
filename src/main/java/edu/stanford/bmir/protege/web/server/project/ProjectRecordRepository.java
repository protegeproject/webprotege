package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public interface ProjectRecordRepository extends CrudRepository<ProjectRecord, ProjectId> {

}

package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 * <p>
 *     An interface to a repository for storing {@link ProjectEntityCrudKitSettings}.
 * </p>
 */
public interface ProjectEntityCrudKitSettingsRepository extends CrudRepository<ProjectEntityCrudKitSettings, ProjectId> {

}

package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of {@link Converter} that reads a {@link ProjectId} from a {@link String}.
 * </p>
 */
public class ProjectIdReadConverter implements Converter<String, ProjectId> {

    public ProjectId convert(String id) {
        return ProjectId.get(id);
    }
}
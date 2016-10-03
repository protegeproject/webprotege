package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of {@link Converter} that writes a {@link ProjectId} to a {@link String}.
 * </p>
 */
public class ProjectIdWriteConverter implements Converter<ProjectId, String> {

    public String convert(ProjectId projectId) {
        return projectId.getId();
    }
}

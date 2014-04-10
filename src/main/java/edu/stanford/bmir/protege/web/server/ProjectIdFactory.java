package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.UUID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class ProjectIdFactory {

    public static ProjectId getFreshProjectId() {
        UUID uuid = UUID.randomUUID();
        return ProjectId.get(uuid.toString());
    }

}

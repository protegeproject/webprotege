package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/03/16
 */
public class ProjectRecordTranslator {


    public static ProjectDetails translateToProjectDetails(ProjectRecord projectRecord) {
        return new ProjectDetails(
                projectRecord.getProjectId(),
                projectRecord.getDisplayName(),
                projectRecord.getDescription(),
                projectRecord.getOwner(),
                projectRecord.isInTrash()
        );
    }
}

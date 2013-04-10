package edu.stanford.bmir.protege.web.client.ui.projectlist;

import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public class ProjectListEntry {

    private ProjectDetails projectDetails;

    public ProjectListEntry(ProjectDetails projectDetails) {
        this.projectDetails = checkNotNull(projectDetails);
    }

    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }
}

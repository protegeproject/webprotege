package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2013
 */
public interface ProjectDetailsFilter {

    boolean isIncluded(ProjectDetails projectDetails);
}

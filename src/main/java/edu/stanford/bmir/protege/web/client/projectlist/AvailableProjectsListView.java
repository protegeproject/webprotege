package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public interface AvailableProjectsListView extends IsWidget, HasSelectionHandlers<ProjectId> {

    void setListData(List<AvailableProject> projectDetails);

    void addListData(AvailableProject projectDetails);

    void setSelectedProject(ProjectId project);
}

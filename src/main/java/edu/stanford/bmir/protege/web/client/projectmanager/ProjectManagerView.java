package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface ProjectManagerView extends HasCreateProjectRequestHandler, HasUploadProjectRequestHandler, HasSelectionHandlers<ProjectId>, HasViewCategoryChangedHandler, IsWidget {

    void setSelectedProject(ProjectId projectId);

    void setAvailableProjects(List<AvailableProject> data);

    void addAvailableProject(AvailableProject availableProject);

    void setCreateProjectEnabled(boolean enabled);

    void setUploadProjectEnabled(boolean enabled);

    void setViewFilters(List<ProjectManagerViewFilter> viewFilters);

    List<ProjectManagerViewFilter> getViewFilters();

    AcceptsOneWidget getLoggedInUserButton();
}

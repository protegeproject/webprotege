package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface ProjectManagerView extends HasLoadProjectRequestHandler, HasCreateProjectRequestHandler, HasUploadProjectRequestHandler, HasDownloadProjectRequestHandler, HasTrashManagerRequestHandler, HasSelectionHandlers<ProjectId>, HasViewCategoryChangedHandler, IsWidget {

    void setSelectedProject(ProjectId projectId);

    void setProjectListData(List<ProjectDetails> data);

    void addProjectData(ProjectDetails details);

    void setCreateProjectEnabled(boolean enabled);

    void setUploadProjectEnabled(boolean enabled);

    void setViewFilters(List<ProjectManagerViewFilter> viewFilters);

    List<ProjectManagerViewFilter> getViewFilters();

    AcceptsOneWidget getLoggedInUserButton();
}

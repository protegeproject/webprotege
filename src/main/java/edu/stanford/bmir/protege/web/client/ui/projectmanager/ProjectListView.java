package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface ProjectListView extends HasLoadProjectRequestHandler, HasCreateProjectRequestHandler, HasUploadProjectRequestHandler, HasDownloadProjectRequestHandler, HasTrashManagerRequestHandler, HasSelectionHandlers<ProjectId>, HasViewCategoryChangedHandler, IsWidget {

    Widget getWidget();

    void setSelectedProject(ProjectId projectId);

    void setProjectListData(List<ProjectDetails> data);

    void addProjectData(ProjectDetails details);

    void setCreateProjectEnabled(boolean enabled);

    void setUploadProjectEnabled(boolean enabled);

    void setViewCategories(List<ProjectManagerViewCategory> viewCategories);
}

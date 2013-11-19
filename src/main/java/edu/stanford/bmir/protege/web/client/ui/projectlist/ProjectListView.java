package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.HasDownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.HasLoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.HasTrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public interface ProjectListView extends IsWidget, HasSelectionHandlers<ProjectId>, HasLoadProjectRequestHandler, HasDownloadProjectRequestHandler, HasTrashManagerRequestHandler {

    void setListData(List<ProjectDetails> projectDetails);

    void setSelectedProject(ProjectId project);
}

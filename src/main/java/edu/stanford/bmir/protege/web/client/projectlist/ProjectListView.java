package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 */
public interface ProjectListView extends IsWidget, HasSelectionHandlers<ProjectId> {

    void setListData(List<ProjectDetails> projectDetails);

    void addListData(ProjectDetails projectDetails);

    void setSelectedProject(ProjectId project);
}

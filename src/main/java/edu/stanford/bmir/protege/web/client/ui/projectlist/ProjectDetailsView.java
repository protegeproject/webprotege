package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public interface ProjectDetailsView extends IsWidget {

    void setProject(ProjectId project, String projectName);

    void setProjectOwner(UserId userId);

    void setDescription(String description);

    void addAction(UIAction uiAction);

    void setInTrash(boolean inTrash);

    void setLoadProjectRequestHandler(LoadProjectRequestHandler loadProjectRequestHandler);
}

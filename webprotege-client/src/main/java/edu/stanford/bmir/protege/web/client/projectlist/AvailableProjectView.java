package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public interface AvailableProjectView extends IsWidget, HasDispose {

    void setProject(ProjectId project, String projectName);

    void setProjectOwner(UserId userId);

    void setModifiedAt(String modifiedAt);

    void setLastOpenedAt(String lastOpenedAt);

    void setDescription(String description);

    void addAction(UIAction uiAction);

    void setInTrash(boolean inTrash);

    void setLoadProjectRequestHandler(LoadProjectRequestHandler loadProjectRequestHandler);
}

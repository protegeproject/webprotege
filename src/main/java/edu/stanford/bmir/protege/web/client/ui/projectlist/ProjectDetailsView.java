package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public interface ProjectDetailsView extends IsWidget {

    void setProjectName(String projectName);

    void setProjectOwner(UserId userId);

    void setDescription(String description);

    void addAction(UIAction uiAction);
}

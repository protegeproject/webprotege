package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 * <p>
 *     A pieces of user interface that provides a method of accessing various settings that are related to a project.
 * </p>
 */
public interface ProjectActionBar extends IsWidget {

    void setProjectId(Optional<ProjectId> projectId);

    void setShowProjectDetailsHandler(ShowProjectDetailsHandler showProjectDetailsHandler);

    void setShowFreshEntitySettingsHandler(ShowFreshEntitySettingsHandler showNewEntitiesHandler);

    void setShowNotificationSettingsHandler(ShowNotificationSettingsHandler showNotificationSettingsHandler);

    void setShowShareSettingsHandler(ShowShareSettingsHandler showShareSettingsHandler);

    void setUploadAndMergeHandler(UploadAndMergeHandler uploadAndMergeHandler);
}

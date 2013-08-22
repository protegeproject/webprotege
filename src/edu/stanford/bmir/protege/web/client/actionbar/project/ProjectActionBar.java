package edu.stanford.bmir.protege.web.client.actionbar.project;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public interface ProjectActionBar {

    void setShowProjectDetailsHandler(ShowProjectDetailsHandler showProjectDetailsHandler);

    void setShowNewEntitySettingsHandler(ShowNewEntitySettingsHandler showNewEntitiesHandler);

    void setShowNotificationSettingsHandler(ShowNotificationSettingsHandler showNotificationSettingsHandler);

    void setShowShareSettingsHandler(ShowShareSettingsHandler showShareSettingsHandler);
}

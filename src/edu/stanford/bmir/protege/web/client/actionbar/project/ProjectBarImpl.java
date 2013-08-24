package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ProjectBarImpl extends Composite implements ProjectActionBar {

    interface ProjectBarImplUiBinder extends UiBinder<HTMLPanel, ProjectBarImpl> {

    }

    private static ProjectBarImplUiBinder ourUiBinder = GWT.create(ProjectBarImplUiBinder.class);

    public ProjectBarImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setProjectId(Optional<ProjectId> projectId) {
        setVisible(projectId.isPresent());
    }

    @Override
    public void setShowProjectDetailsHandler(ShowProjectDetailsHandler showProjectDetailsHandler) {
    }

    @Override
    public void setShowNewEntitySettingsHandler(ShowNewEntitySettingsHandler showNewEntitiesHandler) {
    }

    @Override
    public void setShowNotificationSettingsHandler(ShowNotificationSettingsHandler showNotificationSettingsHandler) {
    }

    @Override
    public void setShowShareSettingsHandler(ShowShareSettingsHandler showShareSettingsHandler) {
    }
}
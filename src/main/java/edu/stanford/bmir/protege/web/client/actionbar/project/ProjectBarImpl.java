package edu.stanford.bmir.protege.web.client.actionbar.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class ProjectBarImpl extends Composite implements ProjectActionBar {

    private ShowShareSettingsHandler showShareSettingsHandler = new ShowShareSettingsHandler() {
        @Override
        public void handleShowShareSettings() {
        }
    };

    private ShowFreshEntitySettingsHandler showFreshEntitySettingsHandler = new ShowFreshEntitySettingsHandler() {
        @Override
        public void handleShowFreshEntitySettings() {
        }
    };

    private ShowProjectDetailsHandler showProjectDetailsHandler = new ShowProjectDetailsHandler() {
        @Override
        public void handleShowProjectDetails() {
        }
    };


    interface ProjectBarImplUiBinder extends UiBinder<HTMLPanel, ProjectBarImpl> {

    }

    private static ProjectBarImplUiBinder ourUiBinder = GWT.create(ProjectBarImplUiBinder.class);

    public ProjectBarImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiField
    protected ButtonBase shareProjectItem;

    @UiField
    protected ButtonBase projectSettingsItem;



    @UiHandler("shareProjectItem")
    protected void handleShareProjectItemClicked(ClickEvent clickEvent) {
        showShareSettingsHandler.handleShowShareSettings();
    }

    @UiHandler("projectSettingsItem")
    protected void handleProjectSettingsItemClicked(ClickEvent clickEvent) {
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem("Project settings...", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showProjectDetailsHandler.handleShowProjectDetails();
            }
        });

        popupMenu.addItem("New entity settings...", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showFreshEntitySettingsHandler.handleShowFreshEntitySettings();
            }
        });
        popupMenu.showRelativeTo(projectSettingsItem);
    }

    @Override
    public void setProjectId(Optional<ProjectId> projectId) {
        setVisible(projectId.isPresent() && Application.get().isLoggedInUserOwnerOfActiveProject());
    }

    @Override
    public void setShowProjectDetailsHandler(ShowProjectDetailsHandler showProjectDetailsHandler) {
        this.showProjectDetailsHandler = checkNotNull(showProjectDetailsHandler);
    }

    @Override
    public void setShowFreshEntitySettingsHandler(ShowFreshEntitySettingsHandler showNewEntitiesHandler) {
        this.showFreshEntitySettingsHandler = checkNotNull(showNewEntitiesHandler);
    }

    @Override
    public void setShowNotificationSettingsHandler(ShowNotificationSettingsHandler showNotificationSettingsHandler) {
    }

    @Override
    public void setShowShareSettingsHandler(ShowShareSettingsHandler showShareSettingsHandler) {
        this.showShareSettingsHandler = checkNotNull(showShareSettingsHandler);
    }
}
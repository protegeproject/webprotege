package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.MenuButton;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class ProjectDetailsViewImpl extends Composite implements ProjectDetailsView {

    interface ProjectDetailsImplUiBinder extends UiBinder<HTMLPanel, ProjectDetailsViewImpl> {

    }

    private static ProjectDetailsImplUiBinder ourUiBinder = GWT.create(ProjectDetailsImplUiBinder.class);

    @UiField
    Label displayNameField;

    @UiField
    Label ownerField;

    @UiField
    MenuButton menuButton;

    private ProjectId projectId;

    private LoadProjectRequestHandler loadProjectRequestHandler = new LoadProjectRequestHandler() {
        @Override
        public void handleProjectLoadRequest(ProjectId projectId) {

        }
    };

    private final List<UIAction> actions = new ArrayList<>();

    public ProjectDetailsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("menuButton")
    protected void handleMenuButtonClicked(ClickEvent event) {
        PopupMenu popupMenu = new PopupMenu();
        for(final UIAction action : actions) {
            popupMenu.addItem(action.getLabel(), new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    action.execute();
                }
            });
        }
        popupMenu.showRelativeTo(menuButton);
    }

    @Override
    public void setLoadProjectRequestHandler(LoadProjectRequestHandler loadProjectRequestHandler) {
        this.loadProjectRequestHandler = loadProjectRequestHandler;
    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameFieldClicked(ClickEvent event) {
        loadProjectRequestHandler.handleProjectLoadRequest(projectId);
    }

    @Override
    public void setProject(ProjectId projectId, String projectName) {
        this.projectId = projectId;
        displayNameField.setText(projectName);
    }

    @Override
    public void setProjectOwner(UserId userId) {
        ownerField.setText(userId.getUserName());
    }

    @Override
    public void setDescription(String description) {
//        descriptionField.setText(description);
    }

    @Override
    public void addAction(UIAction uiAction) {
        actions.add(uiAction);
    }

    @Override
    public void setInTrash(boolean inTrash) {
        if(inTrash) {
            addStyleName(WebProtegeClientBundle.BUNDLE.style().inTrash());
        }
        else {
            removeStyleName(WebProtegeClientBundle.BUNDLE.style().inTrash());
        }
    }
}
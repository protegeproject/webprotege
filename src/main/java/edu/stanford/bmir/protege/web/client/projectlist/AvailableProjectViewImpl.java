package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.library.popupmenu.MenuButton;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class AvailableProjectViewImpl extends Composite implements AvailableProjectView {

    interface AvailableProjectViewImplUiBinder extends UiBinder<HTMLPanel, AvailableProjectViewImpl> {

    }

    private static AvailableProjectViewImplUiBinder ourUiBinder = GWT.create(AvailableProjectViewImplUiBinder.class);

    @UiField
    Label displayNameField;

    @UiField
    Label ownerField;

    @UiField
    SimplePanel ownerIconField;

    @UiField
    Label modifiedAtField;

    @UiField
    MenuButton menuButton;

    private ProjectId projectId;

    @Nonnull
    private LoadProjectRequestHandler loadProjectRequestHandler = pId -> {};

    private final List<UIAction> actions = new ArrayList<>();

    @Inject
    public AvailableProjectViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("menuButton")
    protected void handleMenuButtonClicked(ClickEvent event) {
        PopupMenu popupMenu = new PopupMenu();
        for(final UIAction action : actions) {
            popupMenu.addItem(action);
        }
        popupMenu.showRelativeTo(menuButton);
    }

    @Override
    public void setLoadProjectRequestHandler(@Nonnull LoadProjectRequestHandler loadProjectRequestHandler) {
        this.loadProjectRequestHandler = checkNotNull(loadProjectRequestHandler);
    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameFieldClicked(ClickEvent event) {
        loadProjectRequestHandler.handleProjectLoadRequest(projectId);
    }

    @Override
    public void setProject(@Nonnull ProjectId projectId, @Nonnull String projectName) {
        this.projectId = projectId;
        displayNameField.setText(projectName);
    }

    @Override
    public void setProjectOwner(UserId userId) {
        ownerField.setText(userId.getUserName());
        ownerIconField.setWidget(UserIcon.get(userId));
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public void setModifiedAt(String modifiedAt) {
        modifiedAtField.setText(modifiedAt);
    }

    @Override
    public void addAction(UIAction uiAction) {
        actions.add(uiAction);
    }

    @Override
    public void setInTrash(boolean inTrash) {
        if(inTrash) {
            displayNameField.addStyleName(WebProtegeClientBundle.BUNDLE.style().inTrash());
            ownerField.addStyleName(WebProtegeClientBundle.BUNDLE.style().inTrash());
        }
        else {
            displayNameField.removeStyleName(WebProtegeClientBundle.BUNDLE.style().inTrash());
            ownerField.removeStyleName(WebProtegeClientBundle.BUNDLE.style().inTrash());
        }
    }
}
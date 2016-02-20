package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.MenuButton;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
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

//    @UiField
//    Label descriptionField;
//
    @UiField
    MenuButton menuButton;


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
    public void setProjectName(String projectName) {
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
}
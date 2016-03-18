package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.ui.UIAction;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class ProjectMenuViewImpl extends Composite implements ProjectMenuView {

    interface ProjectMenuViewImplUiBinder extends UiBinder<HTMLPanel, ProjectMenuViewImpl> {

    }

    private static ProjectMenuViewImplUiBinder ourUiBinder = GWT.create(ProjectMenuViewImplUiBinder.class);

    private PopupMenu popupMenu = new PopupMenu();

    public ProjectMenuViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected Button projectMenuButton;

    @UiHandler("projectMenuButton")
    protected void handleProjectMenuClicked(ClickEvent event) {
        popupMenu.showRelativeTo(projectMenuButton);
    }

    @Override
    public void addMenuAction(final UIAction uiAction) {
        popupMenu.addItem(uiAction);
    }
}
package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectmanager;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.*;
import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist.ProjectListDisplay;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist.ProjectListDisplayFactory;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class ProjectManagerDisplayImpl implements ProjectManagerDisplay {

    public static final double TOOL_BAR_PERCENTAGE = 0.2;

    public static final double TABLE_PERCENTAGE = 1 - TOOL_BAR_PERCENTAGE;


    private final ProjectSidePanelDisplay projectSidePanelDisplay = new ProjectSidePanelDisplayImpl();

    private final ProjectListDisplay projectListDisplay = ProjectListDisplayFactory.createProjectListDisplay();



    private final Panel borderLayoutPanel;

    public ProjectManagerDisplayImpl() {
         borderLayoutPanel = new Panel();
        BorderLayout layout = new BorderLayout();
        borderLayoutPanel.setLayout(layout);
        projectSidePanelDisplay.getDisplayWidget().addStyleName("web-protege-menu-bar-button");
        borderLayoutPanel.add(projectSidePanelDisplay.getDisplayWidget(), new BorderLayoutData(RegionPosition.WEST));
        borderLayoutPanel.add(projectListDisplay.getDisplayWidget(), new BorderLayoutData(RegionPosition.CENTER));

//        borderLayoutPanel.add(new Button("Centre"), new BorderLayoutData(RegionPosition.CENTER));
//
//        borderLayoutPanel.add(new Button("West"), new BorderLayoutData(RegionPosition.WEST));
//        borderLayoutPanel.setSize(500, 500);
    }

    public Widget getDisplayWidget() {
        return borderLayoutPanel;
    }

    public void refresh() {
        String userName = GlobalSettings.getGlobalSettings().getUserName();

    }
}

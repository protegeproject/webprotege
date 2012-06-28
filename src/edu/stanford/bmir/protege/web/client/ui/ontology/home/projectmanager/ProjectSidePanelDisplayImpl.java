package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectmanager;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class ProjectSidePanelDisplayImpl implements ProjectSidePanelDisplay {


    public static final String CREATE_PROJECT_BUTTON_LABEL = "Create project...";

    public static final String UPLOAD_PROJECT_BUTTON_LABEL = "Upload project...";


    private final Button createProjectButton;

    private final Button uploadProjectButton;

    private final Panel flowPanel;



    public ProjectSidePanelDisplayImpl() {
        flowPanel = new Panel();
        flowPanel.setLayout(new VerticalLayout());
        flowPanel.addStyleName("tool-bar-debug");

        createProjectButton = new Button(CREATE_PROJECT_BUTTON_LABEL);

//        SimplePanel createButtonWrapper = new SimplePanel();
//        createButtonWrapper.add(createProjectButton);
//        createProjectButton.setWidth("100%");
        flowPanel.add(createProjectButton);


        uploadProjectButton = new Button(UPLOAD_PROJECT_BUTTON_LABEL);
        flowPanel.add(uploadProjectButton);
    }

    public Widget getDisplayWidget() {
        return flowPanel;
    }

    public void refresh() {
    }
}

package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPortlet extends AbstractOWLEntityPortlet {

    private ProjectFeedBasePanel basePanel;

    public ProjectFeedPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }

    @Override
    public void initialize() {
        basePanel = new ProjectFeedBasePanel(getProjectId(), this, getSelectionModel());
        setTitle("Project feed");
        setSize(300, 180);
        add(basePanel);
    }

    @Override
    protected boolean hasRefreshButton() {
        return false;
    }

}

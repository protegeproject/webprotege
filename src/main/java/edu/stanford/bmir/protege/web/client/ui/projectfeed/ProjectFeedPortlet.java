package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPortlet extends AbstractOWLEntityPortlet {

    private ProjectFeedBasePanel basePanel;

    @Inject
    public ProjectFeedPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserManager) {
        super(selectionModel, eventBus, projectId, loggedInUserManager);
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

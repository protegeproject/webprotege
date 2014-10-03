package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPortlet extends AbstractOWLEntityPortlet {

    private ProjectFeedBasePanel basePanel;

    public ProjectFeedPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        basePanel = new ProjectFeedBasePanel(getProjectId(), this);
        setTitle("Project feed");
        setSize(300, 180);
        add(basePanel);
    }

    @Override
    protected boolean hasRefreshButton() {
        return false;
    }

}

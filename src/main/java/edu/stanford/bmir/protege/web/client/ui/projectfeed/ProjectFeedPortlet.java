package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
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

    private static final String HIDE_MY_ACTIVITY = "Hide my activity";

    private static final String SHOW_MY_ACTIVITY = "Show my activity";

    private static final String SHOW_ONTOLOGY_CHANGES = "Show ontology changes";

    private static final String HIDE_ONTOLOGY_CHANGES = "Hide ontology changes";

    private final PortletAction showMyActivityAction;

    private final PortletAction showOntologyChangesAction;

    private boolean showMyActivity = true;

    private boolean showOntologyChanges = true;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ProjectFeedBasePanel basePanel;

    @Inject
    public ProjectFeedPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserManager) {
        super(selectionModel, eventBus, projectId, loggedInUserManager);
        this.loggedInUserProvider = loggedInUserManager;
        basePanel = new ProjectFeedBasePanel(getProjectId(), this, selectionModel);
        setTitle("Project feed");
        getContentHolder().setWidget(basePanel);
        showMyActivityAction = new PortletAction(HIDE_MY_ACTIVITY, new PortletActionHandler() {
            @Override
            public void handleActionInvoked(PortletAction action, ClickEvent event) {
                updateShowMyActivity();
            }
        });

        showOntologyChangesAction = new PortletAction(HIDE_ONTOLOGY_CHANGES, new PortletActionHandler() {
            @Override
            public void handleActionInvoked(PortletAction action, ClickEvent event) {
                updateShowOntologyChanges();
            }
        });

        addPortletAction(showMyActivityAction);
        addPortletAction(showOntologyChangesAction);
    }

    private void updateShowMyActivity() {
        showMyActivity = !showMyActivity;
        basePanel.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showMyActivity);
        if(showMyActivity) {
            showMyActivityAction.setName(HIDE_MY_ACTIVITY);
        }
        else {
            showMyActivityAction.setName(SHOW_MY_ACTIVITY);
        }
    }

    private void updateShowOntologyChanges() {
        showOntologyChanges = !showOntologyChanges;
        basePanel.setOntologyChangesVisible(showOntologyChanges);
        if(showOntologyChanges) {
            showOntologyChangesAction.setName(HIDE_ONTOLOGY_CHANGES);
        }
        else {
            showOntologyChangesAction.setName(SHOW_ONTOLOGY_CHANGES);
        }
    }
}

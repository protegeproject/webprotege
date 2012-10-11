package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import edu.stanford.bmir.protege.web.client.rpc.RevisionManagerService;
import edu.stanford.bmir.protege.web.client.rpc.RevisionManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class RevisionsList extends FlowPanel {

    private static final RevisionManagerServiceAsync revisionManagerService = GWT.create(RevisionManagerService.class);

    private ProjectId projectId;

    private UserData userData;

    public RevisionsList(ProjectId projectId, UserData userData) {
        this.projectId = projectId;
        this.userData = userData;
        reload();
    }


    public void reload() {
        clear();
        revisionManagerService.getRevisionSummaries(projectId, new AsyncCallback<List<RevisionSummary>>() {
            public void onFailure(Throwable caught) {
                Window.alert("There was a problem getting the revisions for this project.  Error: " + caught.getMessage());
            }

            public void onSuccess(List<RevisionSummary> result) {
                refillWithSummaries(result);
            }
        });
    }
    
    
    private void refillWithSummaries(List<RevisionSummary> revisionSummaries) {
        for(RevisionSummary summary : revisionSummaries) {
            add(new RevisionSummaryPanel(projectId, userData, summary));
        }
    }


}

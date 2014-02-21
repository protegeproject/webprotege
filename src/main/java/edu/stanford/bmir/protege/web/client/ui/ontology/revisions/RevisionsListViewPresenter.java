package edu.stanford.bmir.protege.web.client.ui.ontology.revisions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import edu.stanford.bmir.protege.web.client.rpc.RevisionManagerService;
import edu.stanford.bmir.protege.web.client.rpc.RevisionManagerServiceAsync;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.HandlerRegistrationManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public class RevisionsListViewPresenter implements HasDispose {

    private static final RevisionManagerServiceAsync revisionManagerService = GWT.create(RevisionManagerService.class);


    private ProjectId projectId;

    private RevisionsListView view;

    private ListDataProvider<RevisionSummary> dataProvider;

    private final HandlerRegistrationManager registrationManager;


    public RevisionsListViewPresenter(ProjectId projectId, RevisionsListView view) {
        this.view = view;
        this.projectId = projectId;
        dataProvider = new ListDataProvider<RevisionSummary>();
        view.setDataProvider(dataProvider);
        view.setDownloadRevisionRequestHandler(new DownloadRevisionRequestHandlerImpl(projectId));

        registrationManager = new HandlerRegistrationManager();
        registrationManager.registerHandlerToProject(projectId, ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                addRevisionSummary(event.getRevisionSummary());
            }
        });
    }

    public Widget getWidget() {
        return view.getWidget();
    }




    public void reload() {
        revisionManagerService.getRevisionSummaries(projectId, new AsyncCallback<List<RevisionSummary>>() {
            public void onFailure(Throwable caught) {
                MessageBox.showErrorMessage("An error occurred retrieving revision information", caught);
                setListData(Collections.<RevisionSummary>emptyList());
            }

            public void onSuccess(List<RevisionSummary> result) {
                setListData(result);
            }
        });
    }


    private void addRevisionSummary(RevisionSummary revisionSummary) {
        List<RevisionSummary> data = dataProvider.getList();
        if(isNewRevision(revisionSummary, data)) {
            GWT.log("Adding revision: " + revisionSummary);
            data.add(0, revisionSummary);
            dataProvider.flush();
        }
    }

    private boolean isNewRevision(RevisionSummary revisionSummary, List<RevisionSummary> data) {
        return data.isEmpty() || data.get(0).getRevisionNumber().getValue() < revisionSummary.getRevisionNumber().getValue();
    }


    private void setListData(List<RevisionSummary> result) {
        Collections.sort(result);
        Collections.reverse(result);
        List<RevisionSummary> data = dataProvider.getList();
        data.clear();
        data.addAll(result);
        dataProvider.flush();
    }

    @Override
    public void dispose() {
        registrationManager.removeHandlers();
        view.dispose();
    }
}

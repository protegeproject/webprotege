package edu.stanford.bmir.protege.web.client.reasoning;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.reasoning.ExecuteDLQueryAction;
import edu.stanford.bmir.protege.web.shared.reasoning.ExecuteDLQueryResult;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class DLQueryPresenter {

    private ProjectId projectId;

    private DispatchServiceManager dispatchServiceManager;

    private DLQueryView view;

    private ExecuteDLQueryHandler executeDLQueryHandler = new ExecuteDLQueryHandler() {
        @Override
        public void handleExecuteQuery(String query) {
            executeQuery(query);
        }
    };

    @Inject
    public DLQueryPresenter(ProjectId projectId, DispatchServiceManager dispatchServiceManager, DLQueryView dlQueryView) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.view = dlQueryView;
        view.setExecuteDLQueryHandler(executeDLQueryHandler);
    }

    public DLQueryView getView() {
        return view;
    }

    public void executeQuery(String query) {
//        view.setMode(DLQueryViewMode.EXECUTING_QUERY);
        view.clearResults();
        dispatchServiceManager.execute(new ExecuteDLQueryAction(projectId, query, view.getFilter()), new AsyncCallback<ExecuteDLQueryResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Error executing DL query", caught);
                view.setMode(DLQueryViewMode.READY);
            }

            @Override
            public void onSuccess(ExecuteDLQueryResult result) {
                GWT.log("Got result to query: " + result);
                view.setResult(result.getResult());
                view.setMode(DLQueryViewMode.READY);
            }
        });
    }


}

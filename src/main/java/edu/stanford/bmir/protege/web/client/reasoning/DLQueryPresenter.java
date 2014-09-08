package edu.stanford.bmir.protege.web.client.reasoning;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.reasoning.*;

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

    private Optional<String> currentQuery = Optional.absent();

    private boolean waitingForReasoner = false;

    @Inject
    public DLQueryPresenter(ProjectId projectId, DispatchServiceManager dispatchServiceManager, DLQueryView dlQueryView) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.view = dlQueryView;
        view.setExecuteDLQueryHandler(executeDLQueryHandler);
        final EventBusManager manager = EventBusManager.getManager();
        manager.registerHandler(ReasonerReadyEvent.TYPE, new ReasonerReadyHandler() {
            @Override
            public void handleReasonerReady(ReasonerReadyEvent event) {
                GWT.log("The reasoner is ready.  Waiting for reasoner: " + waitingForReasoner + " Current query: " + currentQuery);
                if(waitingForReasoner && currentQuery.isPresent()) {
                    executeQuery(currentQuery.get());
                }
            }
        });
    }

    public void setQueryString(String query) {
        view.setQueryString(query);
    }

    public DLQueryView getView() {
        return view;
    }

    public void executeQuery(final String query) {
        currentQuery = Optional.of(query);
        waitingForReasoner = false;
        view.setMode(DLQueryViewMode.EXECUTING_QUERY);
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
                updateViewToDisplayResult(result.getResult());
            }
        });
    }


    private void updateViewToDisplayResult(ReasonerResult<DLQueryResult> result) {
        view.setMode(DLQueryViewMode.READY);
        result.accept(new ReasonerResultVisitor<DLQueryResult>() {
            @Override
            public void visit(ReasoningUnavailable<DLQueryResult> result) {
                view.setReasoningUnavailable();
            }

            @Override
            public void visit(ReasonerBusy<DLQueryResult> result) {
                view.setReasonerBusy();
                waitingForReasoner = true;
            }

            @Override
            public void visit(ProjectInconsistent<DLQueryResult> result) {
                view.setProjectInconsistent();
                waitingForReasoner = false;
            }

            @Override
            public void visit(ReasonerQueryResult<DLQueryResult> result) {
                view.setResult(result.getResult(), result.getRevisionNumber());
                waitingForReasoner = false;
            }

            @Override
            public void visit(ReasonerError<DLQueryResult> result) {
                view.setReasonerError("An error occurred with the reasoner and the result could not be computed");
                waitingForReasoner = false;
            }

            @Override
            public void visit(ReasonerTimeOut<DLQueryResult> result) {
                view.setReasonerError("The reasoner timed out");
            }

            @Override
            public void visit(MalformedQuery<DLQueryResult> result) {
                view.setReasonerError("The query is malformed:<br>" + result.getErrorMessage());
            }
        });
    }


}

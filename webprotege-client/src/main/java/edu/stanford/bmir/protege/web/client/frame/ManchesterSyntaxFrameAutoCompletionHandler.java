package edu.stanford.bmir.protege.web.client.frame;

import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionCallback;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionHandler;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameCompletionsResult;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class ManchesterSyntaxFrameAutoCompletionHandler implements AutoCompletionHandler {

    private DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;

    private HasFreshEntities hasFreshEntities;

    private HasSubject<OWLEntity> hasSubject;

    private DispatchErrorMessageDisplay errorDisplay;

    @Inject
    public ManchesterSyntaxFrameAutoCompletionHandler(DispatchServiceManager dispatchServiceManager, ProjectId projectId, HasFreshEntities hasFreshEntities, HasSubject<OWLEntity> hasSubject, DispatchErrorMessageDisplay errorDisplay) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.hasFreshEntities = hasFreshEntities;
        this.hasSubject = hasSubject;
        this.errorDisplay = errorDisplay;
    }

    @Override
    public void getCompletions(String text, EditorPosition editorPosition, int editorIndex, final AutoCompletionCallback callback) {
        dispatchServiceManager.execute(
                new GetManchesterSyntaxFrameCompletionsAction(
                        projectId, hasSubject.getSubject(), editorPosition, text, editorIndex, hasFreshEntities.getFreshEntities(), 25),
                new DispatchServiceCallback<GetManchesterSyntaxFrameCompletionsResult>(errorDisplay) {

                    @Override
                    public void handleSuccess(GetManchesterSyntaxFrameCompletionsResult result) {
                        callback.completionsReady(result.getAutoCompletionResult());
                    }

                    @Override
                    public void handleErrorFinally(Throwable throwable) {
                        callback.completionsReady(AutoCompletionResult.emptyResult());
                    }
                });
    }
}

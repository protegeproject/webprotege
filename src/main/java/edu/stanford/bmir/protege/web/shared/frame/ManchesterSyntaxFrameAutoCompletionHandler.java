package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionCallback;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionHandler;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class ManchesterSyntaxFrameAutoCompletionHandler implements AutoCompletionHandler {

    private DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;

    private HasFreshEntities hasFreshEntities;

    private HasSubject<OWLEntity> hasSubject;

    public ManchesterSyntaxFrameAutoCompletionHandler(DispatchServiceManager dispatchServiceManager, ProjectId projectId, HasFreshEntities hasFreshEntities, HasSubject<OWLEntity> hasSubject) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.hasFreshEntities = hasFreshEntities;
        this.hasSubject = hasSubject;
    }

    @Override
    public void getCompletions(String text, EditorPosition editorPosition, int editorIndex, final AutoCompletionCallback callback) {
        dispatchServiceManager.execute(
                new GetManchesterSyntaxFrameCompletionsAction(
                        projectId, hasSubject.getSubject(), editorPosition, text, editorIndex, hasFreshEntities.getFreshEntities(), 25),
                new AbstractDispatchServiceCallback<GetManchesterSyntaxFrameCompletionsResult>() {

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

package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetAnnotationPropertyFrameResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetAnnotationPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetAnnotationPropertyFrameAction, GetAnnotationPropertyFrameResult> {

    @Inject
    public GetAnnotationPropertyFrameActionHandler(ProjectManager projectManager,
                                                   AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetAnnotationPropertyFrameResult execute(GetAnnotationPropertyFrameAction action, Project project, ExecutionContext executionContext) {
        AnnotationPropertyFrameTranslator translator = new AnnotationPropertyFrameTranslator();
        AnnotationPropertyFrame frame = translator.getFrame(
                project.getRenderingManager().getRendering(action.getSubject()),
                project.getRootOntology(),
                project);
        String label = project.getRenderingManager().getBrowserText(action.getSubject());
        LabelledFrame<AnnotationPropertyFrame> labelledFrame = new LabelledFrame<>(label, frame);
        return new GetAnnotationPropertyFrameResult(labelledFrame);
    }

    @Override
    public Class<GetAnnotationPropertyFrameAction> getActionClass() {
        return GetAnnotationPropertyFrameAction.class;
    }
}

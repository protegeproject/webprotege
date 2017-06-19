package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetObjectPropertyFrameResult;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetObjectPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetObjectPropertyFrameAction, GetObjectPropertyFrameResult> {

    private static final ObjectPropertyFrameTranslator TRANSLATOR = new ObjectPropertyFrameTranslator();

    private static Logger logger = LoggerFactory.getLogger(GetObjectPropertyFrameAction.class);

    @Inject
    public GetObjectPropertyFrameActionHandler(ProjectManager projectManager,
                                               AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetObjectPropertyFrameResult execute(GetObjectPropertyFrameAction action, Project project, ExecutionContext executionContext) {
        FrameActionResultTranslator<ObjectPropertyFrame, OWLObjectPropertyData> translator = new FrameActionResultTranslator<>(
                project.getRenderingManager().getRendering(action.getSubject()),
                project, TRANSLATOR);
        LabelledFrame<ObjectPropertyFrame> f = translator.doIT();
        logger.info(BROWSING,
                     "{} {} retrieved ObjectProperty frame for {} ({})",
                     action.getProjectId(),
                     executionContext.getUserId(),
                     action.getSubject(),
                     f.getDisplayName());
        return new GetObjectPropertyFrameResult(f);
    }

    @Override
    public Class<GetObjectPropertyFrameAction> getActionClass() {
        return GetObjectPropertyFrameAction.class;
    }
}

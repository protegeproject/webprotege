package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetClassFrameAction;
import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetClassFrameActionHandler extends AbstractHasProjectActionHandler<GetClassFrameAction, GetClassFrameResult> {

    public static final ClassFrameTranslator TRANSLATOR = new ClassFrameTranslator();

    private static final Logger logger = LoggerFactory.getLogger(GetClassFrameActionHandler.class);

    @Inject
    public GetClassFrameActionHandler(ProjectManager projectManager,
                                      AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetClassFrameAction> getActionClass() {
        return GetClassFrameAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    protected GetClassFrameResult execute(GetClassFrameAction action, Project project, ExecutionContext executionContext) {
        FrameActionResultTranslator<ClassFrame, OWLClassData> translator = new FrameActionResultTranslator<>(
                project.getRenderingManager().getRendering(action.getSubject()),
                project, TRANSLATOR);
        LabelledFrame<ClassFrame> f = translator.doIT();
        logger.info(BROWSING,
                     "{} {} retrieved Class frame for {} ({})",
                    action.getProjectId(),
                    executionContext.getUserId(),
                    action.getSubject(),
                    f.getDisplayName());
        return new GetClassFrameResult(f);
    }
}

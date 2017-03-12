package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetNamedIndividualFrameActionHandler extends AbstractHasProjectActionHandler<GetNamedIndividualFrameAction, GetNamedIndividualFrameResult> {

    private static final NamedIndividualFrameTranslator TRANSLATOR = new NamedIndividualFrameTranslator();

    @Inject
    public GetNamedIndividualFrameActionHandler(ProjectManager projectManager,
                                                AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetNamedIndividualFrameAction> getActionClass() {
        return GetNamedIndividualFrameAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetNamedIndividualFrameResult execute(GetNamedIndividualFrameAction action,
                                                    Project project,
                                                    ExecutionContext executionContext) {
        NamedIndividualFrameTranslator translator = new NamedIndividualFrameTranslator();
        NamedIndividualFrame frame = translator.getFrame(action.getSubject(), project.getRootOntology(), project);
        RenderingManager renderingManager = project.getRenderingManager();
        String rendering = renderingManager.getShortForm(action.getSubject());
        LabelledFrame<NamedIndividualFrame> labelledFrame = new LabelledFrame<>(rendering, frame);
        return new GetNamedIndividualFrameResult(labelledFrame, new BrowserTextMap(frame, renderingManager));

    }
}

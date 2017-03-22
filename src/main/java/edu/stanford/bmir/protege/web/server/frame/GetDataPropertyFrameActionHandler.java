package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetDataPropertyFrameResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class GetDataPropertyFrameActionHandler extends AbstractHasProjectActionHandler<GetDataPropertyFrameAction, GetDataPropertyFrameResult> {

    @Inject
    public GetDataPropertyFrameActionHandler(ProjectManager projectManager,
                                             AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_CHANGES;
    }

    @Override
    protected GetDataPropertyFrameResult execute(GetDataPropertyFrameAction action, Project project, ExecutionContext executionContext) {
        DataPropertyFrameTranslator translator = new DataPropertyFrameTranslator();
        final DataPropertyFrame frame = translator.getFrame(project.getRenderingManager().getRendering(action.getSubject()),
                                                            project.getRootOntology(), project);
        String displayName = project.getRenderingManager().getBrowserText(action.getSubject());
        BrowserTextMap btm = BrowserTextMap.build(project.getRenderingManager(), frame.getSignature());
        return new GetDataPropertyFrameResult(new LabelledFrame<>(displayName, frame), btm);
    }

    @Override
    public Class<GetDataPropertyFrameAction> getActionClass() {
        return GetDataPropertyFrameAction.class;
    }
}

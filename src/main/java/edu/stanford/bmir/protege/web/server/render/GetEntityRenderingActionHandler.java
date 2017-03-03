package edu.stanford.bmir.protege.web.server.render;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingActionHandler extends AbstractHasProjectActionHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    @Inject
    public GetEntityRenderingActionHandler(ProjectManager projectManager, AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetEntityRenderingResult execute(GetEntityRenderingAction action,
                                               final OWLAPIProject project,
                                               ExecutionContext executionContext) {
        OWLEntity entity = action.getEntity();
        HasGetFrameRendering renderer = project.getRenderingManager();
        return new GetEntityRenderingResult(renderer.getFrameRendering(entity));
    }
}

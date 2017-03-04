package edu.stanford.bmir.protege.web.server.renderer;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/02/15
 */
public class GetEntityDataActionHandler extends AbstractHasProjectActionHandler<GetEntityDataAction, GetEntityDataResult> {

    @Inject
    public GetEntityDataActionHandler(ProjectManager projectManager, AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetEntityDataAction> getActionClass() {
        return GetEntityDataAction.class;
    }

    @Override
    protected GetEntityDataResult execute(GetEntityDataAction action, Project project, ExecutionContext executionContext) {
        ImmutableMap.Builder<OWLEntity, OWLEntityData> builder = ImmutableMap.builder();
        builder.putAll(project.getRenderingManager().getRendering(action.getEntities()));
        return new GetEntityDataResult(builder.build());
    }
}

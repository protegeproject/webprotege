package edu.stanford.bmir.protege.web.server.renderer;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/02/15
 */
public class GetEntityDataActionHandler extends AbstractHasProjectActionHandler<GetEntityDataAction, GetEntityDataResult> {

    @Nonnull
    private final RenderingManager renderer;

    @Inject
    public GetEntityDataActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull RenderingManager renderer) {
        super(accessManager);
        this.renderer = renderer;
    }

    @Override
    public Class<GetEntityDataAction> getActionClass() {
        return GetEntityDataAction.class;
    }

    @Override
    public GetEntityDataResult execute(GetEntityDataAction action, ExecutionContext executionContext) {
        ImmutableMap.Builder<OWLEntity, OWLEntityData> builder = ImmutableMap.builder();
        builder.putAll(renderer.getRendering(action.getEntities()));
        return new GetEntityDataResult(builder.build());
    }
}

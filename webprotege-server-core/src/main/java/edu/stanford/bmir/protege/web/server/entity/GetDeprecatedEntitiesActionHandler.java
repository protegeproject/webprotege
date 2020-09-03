package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.index.DeprecatedEntitiesIndex;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public class GetDeprecatedEntitiesActionHandler extends AbstractProjectActionHandler<GetDeprecatedEntitiesAction, GetDeprecatedEntitiesResult> {

    @Nonnull
    private final DeprecatedEntitiesIndex deprecatedEntitiesIndex;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetDeprecatedEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull RenderingManager renderingManager,
                                              @Nonnull DeprecatedEntitiesIndex deprecatedEntitiesIndex) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.deprecatedEntitiesIndex = deprecatedEntitiesIndex;
    }

    @Nonnull
    @Override
    public Class<GetDeprecatedEntitiesAction> getActionClass() {
        return GetDeprecatedEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetDeprecatedEntitiesAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetDeprecatedEntitiesResult execute(@Nonnull GetDeprecatedEntitiesAction action,
                                               @Nonnull ExecutionContext executionContext) {
        var pageRequest = action.getPageRequest();
        var entityTypes = action.getEntityTypes();
        var deprecatedEntitiesPage = deprecatedEntitiesIndex.getDeprecatedEntities(entityTypes,
                                                                                   pageRequest);
        var entityDataPage = deprecatedEntitiesPage.transform(renderingManager::getRendering);
        return new GetDeprecatedEntitiesResult(entityDataPage);
    }


}

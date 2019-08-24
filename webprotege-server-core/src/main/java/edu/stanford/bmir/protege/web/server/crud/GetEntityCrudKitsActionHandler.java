package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitsActionHandler implements ProjectActionHandler<GetEntityCrudKitsAction, GetEntityCrudKitsResult> {

    @Nonnull
    private final EntityCrudKitRegistry entityCrudKitRegistry;

    @Nonnull
    private final ProjectEntityCrudKitHandlerCache crudKitHandlerCache;

    @Inject
    public GetEntityCrudKitsActionHandler(@Nonnull EntityCrudKitRegistry entityCrudKitRegistry,
                                          @Nonnull ProjectEntityCrudKitHandlerCache crudKitHandlerCache) {
        this.entityCrudKitRegistry = checkNotNull(entityCrudKitRegistry);
        this.crudKitHandlerCache = checkNotNull(crudKitHandlerCache);
    }

    @Nonnull
    @Override
    public Class<GetEntityCrudKitsAction> getActionClass() {
        return GetEntityCrudKitsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetEntityCrudKitsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetEntityCrudKitsResult execute(@Nonnull GetEntityCrudKitsAction action, @Nonnull ExecutionContext executionContext) {
        var currentSettings = crudKitHandlerCache.getHandler().getSettings();
        List<EntityCrudKit<?>> kits = entityCrudKitRegistry.getKits();
        return new GetEntityCrudKitsResult(kits, currentSettings);
    }
}


package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_NEW_ENTITY_SETTINGS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsActionHandler extends AbstractProjectActionHandler<GetEntityCrudKitSettingsAction, GetEntityCrudKitSettingsResult> {

    @Nonnull
    private final ProjectEntityCrudKitHandlerCache crudKitHandlerCache;

    @Inject
    public GetEntityCrudKitSettingsActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull ProjectEntityCrudKitHandlerCache crudKitHandlerCache) {
        super(accessManager);
        this.crudKitHandlerCache = crudKitHandlerCache;
    }

    @Nonnull
    @Override
    public Class<GetEntityCrudKitSettingsAction> getActionClass() {
        return GetEntityCrudKitSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityCrudKitSettingsAction action) {
        return EDIT_NEW_ENTITY_SETTINGS;
    }

    @Nonnull
    @Override
    public GetEntityCrudKitSettingsResult execute(@Nonnull GetEntityCrudKitSettingsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetEntityCrudKitSettingsResult(crudKitHandlerCache.getHandler().getSettings());
    }


}

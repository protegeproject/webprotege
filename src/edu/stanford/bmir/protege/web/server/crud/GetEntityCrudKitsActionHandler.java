package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitsActionHandler implements ActionHandler<GetEntityCrudKitsAction, GetEntityCrudKitsResult> {

    @Override
    public Class<GetEntityCrudKitsAction> getActionClass() {
        return GetEntityCrudKitsAction.class;
    }

    @Override
    public RequestValidator<GetEntityCrudKitsAction> getRequestValidator(GetEntityCrudKitsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetEntityCrudKitsResult execute(GetEntityCrudKitsAction action, ExecutionContext executionContext) {
        List<EntityCrudKit<?>> kits = new ArrayList<EntityCrudKit<?>>();
        for(EntityCrudKitPlugin<?, ?> plugin : EntityCrudKitPluginManager.get().getPlugins()) {
            EntityCrudKit kit = plugin.getEntityCrudKit();
            kits.add(kit);
        }
        return new GetEntityCrudKitsResult(kits);
    }
}


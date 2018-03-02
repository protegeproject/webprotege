package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.inject.Inject;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@ApplicationSingleton
public class EntityCrudKitPluginManager implements HasPlugins<EntityCrudKitPlugin<?,?,?>> {

    @Inject
    public EntityCrudKitPluginManager() {
    }

    public List<EntityCrudKitPlugin<?,?,?>> getPlugins() {
        List<EntityCrudKitPlugin<?,?,?>> plugins = Lists.newArrayList();
        plugins.add(new UUIDEntityCrudKitPlugin());
        plugins.add(new OBOIdSuffixEntityCrudKitPlugin());
        plugins.add(new SuppliedNameSuffixEntityCrudKitPlugin());
        return plugins;
    }
}

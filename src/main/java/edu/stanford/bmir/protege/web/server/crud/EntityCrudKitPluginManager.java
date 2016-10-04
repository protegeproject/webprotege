package edu.stanford.bmir.protege.web.server.crud;

import com.beust.jcommander.internal.Lists;
import edu.stanford.bmir.protege.web.server.HasPlugins;
import edu.stanford.bmir.protege.web.server.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDEntityCrudKitPlugin;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class EntityCrudKitPluginManager implements HasPlugins<EntityCrudKitPlugin<?,?,?>> {

    private static EntityCrudKitPluginManager instance = new EntityCrudKitPluginManager();

    private EntityCrudKitPluginManager() {

    }

    public static EntityCrudKitPluginManager get() {
        return instance;
    }

    public List<EntityCrudKitPlugin<?,?,?>> getPlugins() {
        List<EntityCrudKitPlugin<?,?,?>> plugins = Lists.newArrayList();
        plugins.add(new UUIDEntityCrudKitPlugin());
        plugins.add(new OBOIdSuffixEntityCrudKitPlugin());
        plugins.add(new SuppliedNameSuffixEntityCrudKitPlugin());
        return plugins;
    }
}

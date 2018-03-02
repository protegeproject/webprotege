package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.crud.obo.OBOIdSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDEntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@ApplicationSingleton
public class EntityCrudKitPluginManager implements HasPlugins<EntityCrudKitPlugin<?,?,?>> {

    private final ImmutableList<EntityCrudKitPlugin<?,?,?>> plugins;

    @Inject
    public EntityCrudKitPluginManager(@Nonnull Set<EntityCrudKitPlugin<?,?,?>> plugins) {
        this.plugins = ImmutableList.copyOf(plugins);
    }

    public List<EntityCrudKitPlugin<?,?,?>> getPlugins() {
        return plugins;
    }
}

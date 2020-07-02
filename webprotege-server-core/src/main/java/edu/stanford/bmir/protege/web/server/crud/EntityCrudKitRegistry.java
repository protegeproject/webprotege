package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@ProjectSingleton
public class EntityCrudKitRegistry {

    private List<EntityCrudKit<?>> kits = new ArrayList<>();

    private final Map<EntityCrudKitId, EntityCrudKit<?>> id2CrudKit = new HashMap<>();

    private final Map<EntityCrudKitId, EntityCrudKitPlugin<?,?, ?>> id2Plugin = Maps.newHashMap();

    @Inject
    public EntityCrudKitRegistry(@Nonnull EntityCrudKitPluginManager pluginManager) {
        List<EntityCrudKitPlugin<?,?,?>> plugins = pluginManager.getPlugins();
        for(EntityCrudKitPlugin<?,?,?> plugin : plugins) {
            EntityCrudKit<?> kit = plugin.getEntityCrudKit();
            kits.add(kit);
            id2CrudKit.put(kit.getKitId(), kit);
            id2Plugin.put(kit.getKitId(), plugin);
        }
    }

    public List<EntityCrudKit<?>> getKits() {
        return new ArrayList<>(kits);
    }

    private boolean isValidKitId(EntityCrudKitId id) {
        return id2CrudKit.containsKey(id);
    }

    private void checkValidKitId(EntityCrudKitId id) {
        checkNotNull(id);
        if(!isValidKitId(id)) {
            throw new RuntimeException("Invalid kit id: " + id);
        }
    }

    @SuppressWarnings("unchecked")
    private <S extends EntityCrudKitSuffixSettings> EntityCrudKit<S> getKit(EntityCrudKitId id) {
        checkValidKitId(id);
        return (EntityCrudKit<S>) id2CrudKit.get(id);
    }


    @SuppressWarnings("unchecked")
    private <H extends EntityCrudKitHandler<S, C>, S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitPlugin<H, S, C> getPlugin(EntityCrudKitId kitId) {
        checkValidKitId(kitId);
        return (EntityCrudKitPlugin<H, S, C>) id2Plugin.get(kitId);
    }

    @SuppressWarnings("unchecked")
    public <S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitHandler<S, C> getHandler(EntityCrudKitId id) {
        EntityCrudKit<S> kit = getKit(id);
        return getHandler(EntityCrudKitSettings.get(kit.getDefaultPrefixSettings(), kit.getDefaultSuffixSettings()));
    }

    public <H extends EntityCrudKitHandler<S, C>, S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitHandler<S, C> getHandler(EntityCrudKitSettings<S> settings) {
        EntityCrudKitId kitId = settings.getSuffixSettings().getKitId();
        EntityCrudKitPlugin<H, S, C> plugin = getPlugin(kitId);
        return plugin.getEntityCrudKitHandler(settings);
    }

}

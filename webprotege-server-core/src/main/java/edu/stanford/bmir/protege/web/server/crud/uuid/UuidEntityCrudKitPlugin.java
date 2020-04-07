package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class UuidEntityCrudKitPlugin implements EntityCrudKitPlugin<UuidEntityCrudKitHandler, UuidSuffixSettings, ChangeSetEntityCrudSession> {

    @Nonnull
    private UuidSuffixKit kit;

    @Nonnull
    private final UuidEntityCrudKitHandlerFactory factory;

    @Inject
    public UuidEntityCrudKitPlugin(@Nonnull UuidSuffixKit kit,
                                   @Nonnull UuidEntityCrudKitHandlerFactory factory) {
        this.kit = checkNotNull(kit);
        this.factory = checkNotNull(factory);
    }

    @Override
    public EntityCrudKit<UuidSuffixSettings> getEntityCrudKit() {
        return kit;
    }

    @Override
    public UuidEntityCrudKitHandler getEntityCrudKitHandler() {
        return factory.create(EntityCrudKitPrefixSettings.get(), UuidSuffixSettings.get());
    }

    @Override
    public UuidSuffixSettings getDefaultSettings() {
        return UuidSuffixSettings.get();
    }

    @Override
    public UuidEntityCrudKitHandler getEntityCrudKitHandler(EntityCrudKitSettings<UuidSuffixSettings> settings) {
        return factory.create(settings.getPrefixSettings(), settings.getSuffixSettings());
    }
}


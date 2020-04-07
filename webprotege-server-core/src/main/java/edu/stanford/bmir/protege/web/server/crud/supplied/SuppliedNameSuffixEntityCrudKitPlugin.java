package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SuppliedNameSuffixEntityCrudKitPlugin implements EntityCrudKitPlugin<SuppliedNameSuffixEntityCrudKitHandler, SuppliedNameSuffixSettings, ChangeSetEntityCrudSession> {

    @Nonnull
    private final SuppliedNameSuffixKit kit;

    @Nonnull
    private final SuppliedNameSuffixEntityCrudKitHandlerFactory factory;

    @Inject
    public SuppliedNameSuffixEntityCrudKitPlugin(@Nonnull SuppliedNameSuffixKit kit,
                                                 @Nonnull SuppliedNameSuffixEntityCrudKitHandlerFactory factory) {
        this.kit = checkNotNull(kit);
        this.factory = checkNotNull(factory);
    }

    @Override
    public EntityCrudKit<SuppliedNameSuffixSettings> getEntityCrudKit() {
        return kit;
    }

    @Override
    public EntityCrudKitHandler<SuppliedNameSuffixSettings, ChangeSetEntityCrudSession> getEntityCrudKitHandler() {
        return factory.create(EntityCrudKitPrefixSettings.get(), SuppliedNameSuffixSettings.get());
    }

    @Override
    public EntityCrudKitHandler<SuppliedNameSuffixSettings, ChangeSetEntityCrudSession> getEntityCrudKitHandler(EntityCrudKitSettings<SuppliedNameSuffixSettings> settings) {
        return factory.create(settings.getPrefixSettings(), settings.getSuffixSettings());
    }

    @Override
    public SuppliedNameSuffixSettings getDefaultSettings() {
        return SuppliedNameSuffixSettings.get();
    }
}

package edu.stanford.bmir.protege.web.server.crud.obo;

import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OboIdSuffixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class OBOIdSuffixEntityCrudKitPlugin implements EntityCrudKitPlugin<OBOIdSuffixEntityCrudKitHandler, OboIdSuffixSettings, OBOIdSession> {

    @Nonnull
    private final OBOIdSuffixKit kit;

    @Nonnull
    private final OBOIdSuffixEntityCrudKitHandlerFactory factory;

    @Inject
    public OBOIdSuffixEntityCrudKitPlugin(@Nonnull OBOIdSuffixKit kit,
                                          @Nonnull OBOIdSuffixEntityCrudKitHandlerFactory factory) {
        this.kit = checkNotNull(kit);
        this.factory = checkNotNull(factory);
    }

    @Override
    public EntityCrudKit<OboIdSuffixSettings> getEntityCrudKit() {
        return kit;
    }

    @Override
    public EntityCrudKitHandler<OboIdSuffixSettings, OBOIdSession> getEntityCrudKitHandler() {
        return factory.create(EntityCrudKitPrefixSettings.get(), OboIdSuffixSettings.get());
    }

    @Override
    public EntityCrudKitHandler<OboIdSuffixSettings, OBOIdSession> getEntityCrudKitHandler(EntityCrudKitSettings<OboIdSuffixSettings> settings) {
        return factory.create(settings.getPrefixSettings(), settings.getSuffixSettings());
    }

    @Override
    public OboIdSuffixSettings getDefaultSettings() {
        return OboIdSuffixSettings.get();
    }
}

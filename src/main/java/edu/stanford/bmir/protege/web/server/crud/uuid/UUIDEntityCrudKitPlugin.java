package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class UUIDEntityCrudKitPlugin implements EntityCrudKitPlugin<UUIDEntityCrudKitHandler, UUIDSuffixSettings, ChangeSetEntityCrudSession> {

    @Override
    public EntityCrudKit<UUIDSuffixSettings> getEntityCrudKit() {
        return UUIDSuffixKit.get();
    }

    @Override
    public UUIDEntityCrudKitHandler getEntityCrudKitHandler() {
        return new UUIDEntityCrudKitHandler();
    }

    @Override
    public UUIDSuffixSettings getDefaultSettings() {
        return new UUIDSuffixSettings();
    }

    @Override
    public UUIDEntityCrudKitHandler getEntityCrudKitHandler(EntityCrudKitSettings<UUIDSuffixSettings> settings) {
        return new UUIDEntityCrudKitHandler(settings.getPrefixSettings(), settings.getSuffixSettings());
    }
}


package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitFactory;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UUIDEntityCrudKitFactory implements EntityCrudKitFactory<UUIDEntityCrudKitHandler, UUIDSuffixSettings> {

    @Override
    public EntityCrudKitId getKitId() {
        return UUIDSuffixKit.get().getKitId();
    }

    @Override
    public UUIDEntityCrudKitHandler createEntityCrudKit() {
        return new UUIDEntityCrudKitHandler();
    }

    @Override
    public UUIDEntityCrudKitHandler createEntityCrudKit(EntityCrudKitPrefixSettings prefixSettings, UUIDSuffixSettings suffixSettings) {
        return new UUIDEntityCrudKitHandler(prefixSettings, suffixSettings);
    }

}

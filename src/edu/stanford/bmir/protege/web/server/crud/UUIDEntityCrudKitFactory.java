package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UUIDEntityCrudKitFactory implements EntityCrudKitFactory<UUIDEntityCrudKit, UUIDSuffixSettings> {

    @Override
    public EntityCrudKitId getKitId() {
        return UUIDSuffixDescriptor.get().getKitId();
    }

    @Override
    public UUIDEntityCrudKit createEntityCrudKit() {
        return new UUIDEntityCrudKit();
    }

    @Override
    public UUIDEntityCrudKit createEntityCrudKit(EntityCrudKitPrefixSettings prefixSettings, UUIDSuffixSettings suffixSettings) {
        return new UUIDEntityCrudKit(prefixSettings, suffixSettings);
    }

}

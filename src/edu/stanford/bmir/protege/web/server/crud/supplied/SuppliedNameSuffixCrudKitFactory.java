package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitFactory;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixCrudKitFactory implements EntityCrudKitFactory<SuppliedNameSuffixEntityCrudKitHandler, SuppliedNameSuffixSettings> {

    @Override
    public EntityCrudKitId getKitId() {
        return SuppliedNameSuffixKit.get().getKitId();
    }

    @Override
    public SuppliedNameSuffixEntityCrudKitHandler createEntityCrudKit() {
        return new SuppliedNameSuffixEntityCrudKitHandler();
    }

    @Override
    public SuppliedNameSuffixEntityCrudKitHandler createEntityCrudKit(EntityCrudKitPrefixSettings prefixSettings, SuppliedNameSuffixSettings settings) {
        return new SuppliedNameSuffixEntityCrudKitHandler(prefixSettings, settings);
    }

}

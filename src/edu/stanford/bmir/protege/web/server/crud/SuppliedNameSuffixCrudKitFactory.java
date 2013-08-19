package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixDescriptor;
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
        return SuppliedNameSuffixDescriptor.get().getKitId();
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

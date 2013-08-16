package edu.stanford.bmir.protege.web.shared.crud;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 * <p>
 *
 * </p>
 */
public class UUIDSuffixSettings extends EntityCrudKitSuffixSettings {

    // No settings in particular

    public UUIDSuffixSettings() {
    }


    @Override
    public EntityCrudKitId getKitId() {
        return UUIDSuffixDescriptor.get().getKitId();
    }

}

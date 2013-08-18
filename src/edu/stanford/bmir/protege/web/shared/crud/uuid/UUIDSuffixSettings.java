package edu.stanford.bmir.protege.web.shared.crud.uuid;


import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;

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

    @Override
    public int hashCode() {
        return "UUIDSuffixSettings".hashCode();
    }

    public boolean equals(Object o) {
        return o == this || o instanceof UUIDSuffixSettings;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("UUIDSuffixSettings")
                .toString();
    }

}

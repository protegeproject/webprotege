package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.Objects;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class SuppliedNameSuffixSettings extends EntityCrudKitSuffixSettings {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("ShortFormSuffix");

    public SuppliedNameSuffixSettings() {
    }

    @Override
    public EntityCrudKitId getKitId() {
        return ID;
    }

    @Override
    public int hashCode() {
        return "ShortFormSuffixKitSettings".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SuppliedNameSuffixSettings)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("SuppliedNameSuffixSettings")
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.crud.supplied;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class SuppliedNameSuffixSettings extends EntityCrudKitSuffixSettings {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("ShortFormSuffix");

    private WhiteSpaceTreatment whiteSpaceTreatment;

    public SuppliedNameSuffixSettings() {
        this(WhiteSpaceTreatment.TRANSFORM_TO_CAMEL_CASE);
    }

    public SuppliedNameSuffixSettings(WhiteSpaceTreatment whiteSpaceTreatment) {
        this.whiteSpaceTreatment = whiteSpaceTreatment;
    }

    @Override
    public EntityCrudKitId getKitId() {
        return ID;
    }

    public WhiteSpaceTreatment getWhiteSpaceTreatment() {
        return whiteSpaceTreatment;
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
                .add("whiteSpaceTreatment", whiteSpaceTreatment)
                .toString();
    }
}

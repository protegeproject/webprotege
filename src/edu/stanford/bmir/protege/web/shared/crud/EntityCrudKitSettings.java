package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class EntityCrudKitSettings<S extends EntityCrudKitSuffixSettings> implements Serializable {

    private EntityCrudKitPrefixSettings prefixSettings;

    private S suffixSettings;

    private EntityCrudKitSettings() {
    }

    public EntityCrudKitSettings(EntityCrudKitPrefixSettings prefixSettings, S suffixSettings) {
        this.prefixSettings = prefixSettings;
        this.suffixSettings = suffixSettings;
    }

    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    public S getSuffixSettings() {
        return suffixSettings;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("EntityCrudKitSettings")
                .add("prefixSettings", prefixSettings)
                .add("suffixSettings", suffixSettings)
                .toString();
    }
}

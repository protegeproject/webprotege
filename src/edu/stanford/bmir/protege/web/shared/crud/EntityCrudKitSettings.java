package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.Objects;
import org.springframework.data.annotation.TypeAlias;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@TypeAlias("EntityCrudKitSettings")
public class EntityCrudKitSettings<S extends EntityCrudKitSuffixSettings> implements Serializable {

    private EntityCrudKitPrefixSettings prefixSettings;

    private S suffixSettings;

    private EntityCrudKitSettings() {
    }

    /**
     * Constructs an {@link EntityCrudKitSettings} object for the specified {@link EntityCrudKitPrefixSettings} and
     * {@link EntityCrudKitSuffixSettings}.
     * @param prefixSettings The {@link EntityCrudKitPrefixSettings}. Not {@code null}.
     * @param suffixSettings The {@link EntityCrudKitSuffixSettings}. Not {@code null}.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public EntityCrudKitSettings(EntityCrudKitPrefixSettings prefixSettings, S suffixSettings) {
        this.prefixSettings = checkNotNull(prefixSettings);
        this.suffixSettings = checkNotNull(suffixSettings);
    }

    /**
     * Gets the prefix settings for this settings object.
     * @return The prefix settings.  Not {@code null}.
     */
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    /**
     * Gets the suffix settings for this object.
     * @return The suffix settings. Not {@code null}.
     */
    public S getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public int hashCode() {
        return "EntityCrudKitSettings".hashCode() + prefixSettings.hashCode() + suffixSettings.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof EntityCrudKitSettings)) {
            return false;
        }
        EntityCrudKitSettings other = (EntityCrudKitSettings) o;
        return this.prefixSettings.equals(other.prefixSettings) && this.suffixSettings.equals(other.suffixSettings);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EntityCrudKitSettings")
                .add("prefixSettings", prefixSettings)
                .add("suffixSettings", suffixSettings)
                .toString();
    }
}

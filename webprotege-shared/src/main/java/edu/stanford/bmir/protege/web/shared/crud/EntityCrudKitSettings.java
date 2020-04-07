package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityCrudKitSettings<S extends EntityCrudKitSuffixSettings> implements Serializable {

    public static final String PREFIX_SETTINGS = "prefixSettings";

    public static final String SUFFIX_SETTINGS = "suffixSettings";

    /**
     * Constructs an {@link EntityCrudKitSettings} object for the specified {@link EntityCrudKitPrefixSettings} and
     * {@link EntityCrudKitSuffixSettings}.
     * @param prefixSettings The {@link EntityCrudKitPrefixSettings}. Not {@code null}.
     * @param suffixSettings The {@link EntityCrudKitSuffixSettings}. Not {@code null}.
     * @throws NullPointerException if any argument is {@code null}.
     */
    @JsonCreator
    public static <S extends EntityCrudKitSuffixSettings> EntityCrudKitSettings<S> get(@JsonProperty(PREFIX_SETTINGS) @Nonnull EntityCrudKitPrefixSettings prefixSettings,
                                            @JsonProperty(SUFFIX_SETTINGS) S suffixSettings) {

        return new AutoValue_EntityCrudKitSettings<S>(prefixSettings, suffixSettings);
    }

    /**
     * Gets the prefix settings for this settings object.
     * @return The prefix settings.  Not {@code null}.
     */
    @JsonProperty(PREFIX_SETTINGS)
    public abstract EntityCrudKitPrefixSettings getPrefixSettings();

    /**
     * Gets the suffix settings for this object.
     * @return The suffix settings. Not {@code null}.
     */
    @JsonProperty(SUFFIX_SETTINGS)
    public abstract S getSuffixSettings();
}

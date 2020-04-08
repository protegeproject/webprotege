package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;


import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ConditionalIriPrefix {

    public static final String IRI_PREFIX = "iriPrefix";

    public static ConditionalIriPrefix get() {
        return get(EntityCrudKitPrefixSettings.DEFAULT_IRI_PREFIX);
    }

    @Nonnull
    @JsonCreator
    public static ConditionalIriPrefix get(@JsonProperty(IRI_PREFIX) String iriPrefix) {
        return new AutoValue_ConditionalIriPrefix(iriPrefix);
    }

    @Nonnull
    public abstract String getIriPrefix();

}

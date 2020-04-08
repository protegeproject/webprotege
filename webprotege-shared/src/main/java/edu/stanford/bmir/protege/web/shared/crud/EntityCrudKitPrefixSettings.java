package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasIRIPrefix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityCrudKitPrefixSettings implements HasIRIPrefix, IsSerializable {

    public static final String DEFAULT_IRI_PREFIX = "http://www.example.org/";

    public static final String IRI_PREFIX = "iriPrefix";

    public static final String CONDITIONAL_IRI_PREFIXES = "conditionalIriPrefixes";

    @Nonnull
    public static EntityCrudKitPrefixSettings get() {
        return get(DEFAULT_IRI_PREFIX, ImmutableList.of());
    }

    @Nonnull
    public static EntityCrudKitPrefixSettings get(@Nonnull @JsonProperty(IRI_PREFIX) String iriPrefix,
                                                     @Nonnull @JsonProperty(CONDITIONAL_IRI_PREFIXES) ImmutableList<ConditionalIriPrefix> conditionalIriPrefixes) {
        return new AutoValue_EntityCrudKitPrefixSettings(iriPrefix, conditionalIriPrefixes);
    }

    @JsonCreator
    @Nonnull
    protected static EntityCrudKitPrefixSettings create(@Nonnull @JsonProperty(IRI_PREFIX) String iriPrefix,
                                                        @Nullable @JsonProperty(CONDITIONAL_IRI_PREFIXES) ImmutableList<ConditionalIriPrefix> conditionalIriPrefixes) {
        if(conditionalIriPrefixes == null) {
            return get(iriPrefix, ImmutableList.of());
        }
        else {
            return get(iriPrefix, conditionalIriPrefixes);
        }
    }

    @JsonProperty(IRI_PREFIX)
    @Override
    public abstract String getIRIPrefix();

    @JsonProperty(CONDITIONAL_IRI_PREFIXES)
    public abstract ImmutableList<ConditionalIriPrefix> getConditionalIriPrefixes();

}

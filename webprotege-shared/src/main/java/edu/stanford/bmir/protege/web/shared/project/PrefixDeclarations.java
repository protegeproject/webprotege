package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Feb 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PrefixDeclarations {

    public static final String PROJECT_ID = "_id";

    public static final String PREFIXES = "prefixes";

    /**
     * Gets an empty project prefixes for the specified project id.
     * @param projectId The project id.
     */
    public static PrefixDeclarations get(@Nonnull ProjectId projectId) {
        return new AutoValue_PrefixDeclarations(projectId, ImmutableMap.of());
    }

    /**
     * Creates a project prefixes object for the specified project and the specified prefixes.
     * @param projectId The projectId that identifies the project.
     * @param prefixes The prefixes.  A map of prefix names to prefixes.  Neither prefix names or prefixes are allowed
     *                 to be null.  Prefix names must end with colons.
     * @return The created {@link PrefixDeclarations}.
     */
    @JsonCreator
    public static PrefixDeclarations get(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                         @JsonProperty(PREFIXES) @Nonnull Map<String, String> prefixes) {
        checkNotNull(projectId);
        checkNotNull(prefixes);
        for(Map.Entry<String, String> entry : prefixes.entrySet()) {
            if(entry.getKey() == null) {
                throw new NullPointerException("Null prefix names are not allowed");
            }
            if(!entry.getKey().endsWith(":")) {
                throw new IllegalArgumentException("Prefix names must end with a colon");
            }
            if(entry.getValue() == null) {
                throw new NullPointerException("Prefixes must not be null.  " +
                                                       "Prefix pointed to by " + entry.getKey() + " is null.");
            }
        }
        return new AutoValue_PrefixDeclarations(projectId, ImmutableMap.copyOf(prefixes));
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(PREFIXES)
    @Nonnull
    public abstract ImmutableMap<String, String> getPrefixes();

    @JsonIgnore
    @Nonnull
    public Optional<String> getPrefixForPrefixName(@Nonnull String prefixName) {
        checkArgument(prefixName.endsWith(":"), "Prefix names must end with a colon");
        return Optional.ofNullable(getPrefixes().get(prefixName));
    }
}

package edu.stanford.bmir.protege.web.shared.project;

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
@Entity(noClassnameStored = true)
public class PrefixDeclarations {

    public static final String PROJECT_ID = "_id";

    @Id
    private ProjectId projectId;

    private Map<String, String> prefixes;

    // For Morphia
    private PrefixDeclarations() {
    }

    private PrefixDeclarations(@Nonnull ProjectId projectId,
                               @Nonnull Map<String, String> prefixes) {
        this.projectId = checkNotNull(projectId);
        this.prefixes = ImmutableMap.copyOf(checkNotNull(prefixes));
    }

    /**
     * Gets an empty project prefixes for the specified project id.
     * @param projectId The project id.
     */
    public static PrefixDeclarations get(@Nonnull ProjectId projectId) {
        return new PrefixDeclarations(projectId, ImmutableMap.of());
    }

    /**
     * Creates a project prefixes object for the specified project and the specified prefixes.
     * @param projectId The projectId that identifies the project.
     * @param prefixes The prefixes.  A map of prefix names to prefixes.  Neither prefix names or prefixes are allowed
     *                 to be null.  Prefix names must end with colons.
     * @return The created {@link PrefixDeclarations}.
     */
    public static PrefixDeclarations get(@Nonnull ProjectId projectId,
                                         @Nonnull Map<String, String> prefixes) {
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
        return new PrefixDeclarations(projectId, prefixes);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public Map<String, String> getPrefixes() {
        return ImmutableMap.copyOf(prefixes);
    }

    @Nonnull
    public Optional<String> getPrefixForPrefixName(@Nonnull String prefixName) {
        checkArgument(prefixName.endsWith(":"), "Prefix names must end with a colon");
        return Optional.ofNullable(prefixes.get(prefixName));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, prefixes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PrefixDeclarations)) {
            return false;
        }
        PrefixDeclarations other = (PrefixDeclarations) obj;
        return this.projectId.equals(other.projectId)
                && this.prefixes.equals(other.prefixes);
    }


    @Override
    public String toString() {
        return toStringHelper("PrefixDeclarations")
                          .addValue(projectId)
                          .add("prefixes", prefixes)
                          .toString();
    }
}

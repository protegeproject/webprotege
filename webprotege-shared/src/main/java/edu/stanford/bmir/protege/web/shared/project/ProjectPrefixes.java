package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Feb 2018
 */
@Entity(noClassnameStored = true)
public class ProjectPrefixes {

    public static final String PROJECT_ID = "_id";

    @Id
    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ImmutableMap<String, String > prefixes;

    private ProjectPrefixes(@Nonnull ProjectId projectId,
                           @Nonnull Map<String, String> prefixes) {
        this.projectId = projectId;
        this.prefixes = ImmutableMap.copyOf(prefixes);
    }

    /**
     * Gets an empty project prefixes for the specified project id.
     * @param projectId The project id.
     */
    public static ProjectPrefixes get(@Nonnull ProjectId projectId) {
        return new ProjectPrefixes(projectId, ImmutableMap.of());
    }

    public static ProjectPrefixes get(@Nonnull ProjectId projectId,
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
        return new ProjectPrefixes(projectId, prefixes);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableMap<String, String> getPrefixes() {
        return prefixes;
    }

    @Nonnull
    public Optional<String> getPrefixForPrefixName(@Nonnull String prefixName) {
        return Optional.ofNullable(prefixes.get(prefixName));
    }
}

package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

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

    public ProjectPrefixes(@Nonnull ProjectId projectId,
                           @Nonnull Map<String, String> prefixes) {
        this.projectId = projectId;
        this.prefixes = ImmutableMap.copyOf(prefixes);
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

package edu.stanford.bmir.protege.web.server.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectEntityGraphSettings {

    public static final String PROJECT_ID = "_id";

    @JsonCreator
    public static ProjectEntityGraphSettings get(@Nonnull @JsonProperty(PROJECT_ID) ProjectId projectId,
                                                 @Nonnull @JsonProperty("edgeCriteria") ImmutableList<EdgeCriteria> criteria) {
        return new AutoValue_ProjectEntityGraphSettings(projectId, criteria);
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    /**
     * Gets the criteria that are used for filtering edges in an
     * entity graph
     * @return The criteria.
     */
    @Nonnull
    public abstract ImmutableList<EdgeCriteria> getEdgeCriteria();
}

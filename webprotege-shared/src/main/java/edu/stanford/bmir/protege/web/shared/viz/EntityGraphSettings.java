package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityGraphSettings {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String CRITERIA = "criteria";

    public static final String RANK_SPACING = "rankSpacing";

    @JsonCreator
    public static EntityGraphSettings get(@Nonnull @JsonProperty(PROJECT_ID) ProjectId projectId,
                                          @Nullable @JsonProperty(USER_ID) UserId userId,
                                          @Nonnull @JsonProperty(CRITERIA) EdgeCriteria criteria,
                                          @JsonProperty(value = RANK_SPACING, defaultValue = "1.0") double rankSpacing) {
        return new AutoValue_EntityGraphSettings(projectId, userId, criteria, rankSpacing);
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(USER_ID)
    @Nullable
    protected abstract UserId getUserIdInternal();

    @Nonnull
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }

    /**
     * Gets the criteria that are used for filtering edges in an
     * entity graph
     * @return The criteria.
     */
    @Nonnull
    @JsonProperty(CRITERIA)
    public abstract EdgeCriteria getCriteria();

    @JsonProperty(RANK_SPACING)
    public abstract double getRankSpacing();
}

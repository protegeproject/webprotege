package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntitySearchFilter {

    public static final String ID = "_id";

    public static final String PROJECT_ID = "projectId";

    public static final String LABEL = "label";

    public static final String CRITERIA = "criteria";

    @JsonCreator
    @Nonnull
    public static EntitySearchFilter get(@JsonProperty(ID) @Nonnull EntitySearchFilterId id,
                                         @JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                         @JsonProperty(LABEL) @Nonnull LanguageMap label,
                                         @JsonProperty(CRITERIA) @Nonnull EntityMatchCriteria entityMatchCriteria) {
        return new AutoValue_EntitySearchFilter(id, projectId, label, entityMatchCriteria);
    }

    @JsonProperty(ID)
    @Nonnull
    public abstract EntitySearchFilterId getId();

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(LABEL)
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty(CRITERIA)
    @Nonnull
    public abstract EntityMatchCriteria getEntityMatchCriteria();
}

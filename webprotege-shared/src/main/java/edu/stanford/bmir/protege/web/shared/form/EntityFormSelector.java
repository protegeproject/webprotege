package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class EntityFormSelector {

    @JsonCreator
    public static EntityFormSelector get(@Nonnull @JsonProperty("projectId") ProjectId projectId,
                                         @Nonnull @JsonProperty("criteria") CompositeRootCriteria criteria,
                                         @Nonnull @JsonProperty("formId") FormId formId) {
        return new AutoValue_EntityFormSelector(projectId, criteria, formId);
    }

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("criteria")
    public abstract CompositeRootCriteria getCriteria();

    @JsonProperty("formId")
    public abstract FormId getFormId();

}

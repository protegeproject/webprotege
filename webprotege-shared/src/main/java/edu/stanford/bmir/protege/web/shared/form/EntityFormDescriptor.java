package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
@AutoValue
public abstract class EntityFormDescriptor {


    public static final String PROJECT_ID = "projectId";

    public static final String FORM_ID = "formId";

    public static final String FORM_DESCRIPTOR = "formDescriptor";

    public static final String SELECTOR_CRITERIA = "formSelectorCriteria";


    @JsonCreator
    public static EntityFormDescriptor valueOf(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                               @JsonProperty(FORM_ID) @Nonnull FormId formId,
                                               @JsonProperty(FORM_DESCRIPTOR) @Nonnull FormDescriptor newDescriptor,
                                               @JsonProperty(SELECTOR_CRITERIA) @Nonnull RootCriteria newSelectorCriteria) {
        return new AutoValue_EntityFormDescriptor(projectId, formId, newDescriptor, newSelectorCriteria);
    }

    @Nonnull
    @JsonProperty(PROJECT_ID)
    public abstract ProjectId getProjectId();

    @Nonnull
    @JsonProperty(FORM_ID)
    public abstract FormId getFormId();

    @Nonnull
    @JsonProperty(FORM_DESCRIPTOR)
    public abstract FormDescriptor getDescriptor();

    @Nonnull
    @JsonProperty(SELECTOR_CRITERIA)
    public abstract RootCriteria getSelectorCriteria();
}

package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public class GetDeprecatedEntitiesResult implements Result {

    private Page<OWLEntityData> deprecatedEntities;

    @GwtSerializationConstructor
    private GetDeprecatedEntitiesResult() {
    }

    public GetDeprecatedEntitiesResult(@Nonnull Page<OWLEntityData> deprecatedEntities) {
        this.deprecatedEntities = checkNotNull(deprecatedEntities);
    }

    @Nonnull
    public Page<OWLEntityData> getDeprecatedEntities() {
        return deprecatedEntities;
    }
}

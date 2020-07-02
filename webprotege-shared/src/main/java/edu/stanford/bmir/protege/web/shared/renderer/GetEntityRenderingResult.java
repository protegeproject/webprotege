package edu.stanford.bmir.protege.web.shared.renderer;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingResult implements Result {

    private OWLEntityData entityData;

    @GwtSerializationConstructor
    private GetEntityRenderingResult() {
    }

    public GetEntityRenderingResult(@Nonnull OWLEntityData entityData) {
        this.entityData = checkNotNull(entityData);
    }

    @Nonnull
    public OWLEntityData getEntityData() {
        return entityData;
    }
}

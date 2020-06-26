package edu.stanford.bmir.protege.web.shared.renderer;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
public class GetEntityHtmlRenderingResult implements Result {

    private OWLEntityData entityData;

    private String rendering;

    @GwtSerializationConstructor
    private GetEntityHtmlRenderingResult() {
    }

    public GetEntityHtmlRenderingResult(OWLEntityData entityData,
                                        @Nonnull String rendering) {
        this.entityData = checkNotNull(entityData);
        this.rendering = checkNotNull(rendering);
    }

    @Nonnull
    public OWLEntityData getEntityData() {
        return entityData;
    }

    @Nonnull
    public String getRendering() {
        return rendering;
    }
}

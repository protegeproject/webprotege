package edu.stanford.bmir.protege.web.shared.renderer;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingResult implements Result {

    private String rendering;

    private OWLEntityData entityData;

    private GetEntityRenderingResult() {
    }

    public GetEntityRenderingResult(String rendering, OWLEntityData entityData) {
        this.rendering = checkNotNull(rendering);
        this.entityData = checkNotNull(entityData);
    }

    public String getRendering() {
        return rendering;
    }

    public OWLEntityData getEntityData() {
        return entityData;
    }
}

package edu.stanford.bmir.protege.web.shared.renderer;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingResult implements Result {

    private String rendering;

    private GetEntityRenderingResult() {
    }

    public GetEntityRenderingResult(String rendering) {
        this.rendering = rendering;
    }

    public String getRendering() {
        return rendering;
    }
}

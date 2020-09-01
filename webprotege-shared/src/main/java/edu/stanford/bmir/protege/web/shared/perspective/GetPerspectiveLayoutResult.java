package edu.stanford.bmir.protege.web.shared.perspective;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class GetPerspectiveLayoutResult implements Result {

    private PerspectiveLayout perspective;

    /**
     * For serialization only
     */
    private GetPerspectiveLayoutResult() {
    }

    public GetPerspectiveLayoutResult(PerspectiveLayout perspective) {
        this.perspective = perspective;
    }

    public PerspectiveLayout getPerspective() {
        return perspective;
    }
}

package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class GetPerspectivesResult implements Result {

    private ImmutableList<PerspectiveId> perspectives;

    private GetPerspectivesResult() {
    }

    public GetPerspectivesResult(ImmutableList<PerspectiveId> perspectives) {
        this.perspectives = perspectives;
    }

    public ImmutableList<PerspectiveId> getPerspectives() {
        return perspectives;
    }
}

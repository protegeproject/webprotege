package edu.stanford.bmir.protege.web.shared.reasoning;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class ExecuteDLQueryResult extends AbstractReasonerQueryResult<DLQueryResult> {

    /**
     * For serialization purposes only
     */
    private ExecuteDLQueryResult() {
    }

    public ExecuteDLQueryResult(ProjectId projectId, ReasonerResult<DLQueryResult> reasonerResult) {
        super(projectId, reasonerResult);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ExecuteDLQueryResult")
                      .addValue(getResult()).toString();
    }
}

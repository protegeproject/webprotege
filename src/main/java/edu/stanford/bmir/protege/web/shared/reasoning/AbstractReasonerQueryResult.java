package edu.stanford.bmir.protege.web.shared.reasoning;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public abstract class AbstractReasonerQueryResult<R extends Serializable> implements Result, HasProjectId{

    private ProjectId projectId;

    private ReasonerResult<R> reasonerResult;

    protected AbstractReasonerQueryResult(
            ProjectId projectId, ReasonerResult<R> reasonerResult) {
        this.projectId = projectId;
        this.reasonerResult = checkNotNull(reasonerResult);
    }

    /**
     * For serialization purposes only
     */
    protected AbstractReasonerQueryResult() {
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ReasonerResult<R> getResult() {
        return reasonerResult;
    }
}

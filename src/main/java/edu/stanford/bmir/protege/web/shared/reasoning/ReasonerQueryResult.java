package edu.stanford.bmir.protege.web.shared.reasoning;

import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public class ReasonerQueryResult<R extends Serializable> extends ReasonerResult<R> {

    private RevisionNumber revisionNumber;

    private R result;

    public ReasonerQueryResult(RevisionNumber revisionNumber, R result) {
        this.revisionNumber = revisionNumber;
        this.result = result;
    }

    /**
     * For serialization purposes only
     */
    private ReasonerQueryResult() {
    }

    @Override
    public void accept(ReasonerResultVisitor<R> visitor) {
        visitor.visit(this);
    }

    public R getResult() {
        return result;
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }
}

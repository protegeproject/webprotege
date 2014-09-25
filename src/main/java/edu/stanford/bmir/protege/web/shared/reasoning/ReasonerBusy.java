package edu.stanford.bmir.protege.web.shared.reasoning;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public class ReasonerBusy<R extends Serializable> extends ReasonerResult<R> {

    public ReasonerBusy() {
    }

    @Override
    public void accept(ReasonerResultVisitor<R> visitor) {
        visitor.visit(this);
    }
}

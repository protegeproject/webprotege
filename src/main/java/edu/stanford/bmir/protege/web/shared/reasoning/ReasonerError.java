package edu.stanford.bmir.protege.web.shared.reasoning;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public class ReasonerError<R extends Serializable> extends ReasonerResult<R> {
    @Override
    public void accept(ReasonerResultVisitor<R> visitor) {
        visitor.visit(this);
    }
}

package edu.stanford.bmir.protege.web.shared.reasoning;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public abstract class ReasonerResult<R extends Serializable> implements Result {

    public abstract void accept(ReasonerResultVisitor<R> visitor);
}

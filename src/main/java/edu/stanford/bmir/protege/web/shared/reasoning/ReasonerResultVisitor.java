package edu.stanford.bmir.protege.web.shared.reasoning;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public interface ReasonerResultVisitor<R extends Serializable> {

    void visit(ReasoningUnavailable<R> result);

    void visit(ReasonerBusy<R> result);

    void visit(ProjectInconsistent<R> result);

    void visit(ReasonerQueryResult<R> result);

    void visit(ReasonerTimeOut<R> result);

    void visit(ReasonerError<R> result);

    void visit(MalformedQuery<R> result);
}

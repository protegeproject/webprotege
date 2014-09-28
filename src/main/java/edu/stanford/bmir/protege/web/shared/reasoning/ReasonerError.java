package edu.stanford.bmir.protege.web.shared.reasoning;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public class ReasonerError<R extends Serializable> extends ReasonerResult<R> {

    private String message;

    public ReasonerError() {
        this("Unspecified internal error");
    }

    public ReasonerError(String message) {
        this.message = checkNotNull(message);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void accept(ReasonerResultVisitor<R> visitor) {
        visitor.visit(this);
    }
}

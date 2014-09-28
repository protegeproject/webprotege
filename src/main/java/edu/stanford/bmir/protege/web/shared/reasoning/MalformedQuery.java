package edu.stanford.bmir.protege.web.shared.reasoning;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 28/09/2014
 */
public class MalformedQuery<R extends Serializable> extends ReasonerResult<R> {

    private String errorMessage;

    /**
     * For serialization purposes only
     */
    private MalformedQuery() {
    }

    public MalformedQuery(String errorMessage) {
        this.errorMessage = checkNotNull(errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void accept(ReasonerResultVisitor<R> visitor) {
        visitor.visit(this);
    }
}

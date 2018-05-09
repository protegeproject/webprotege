package edu.stanford.bmir.protege.web.server.api.axioms;

import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 */
public class PostedAxiomsLoadFailureResponse implements PostedAxiomsLoadResponse {

    @Nonnull
    private final Response.Status status;

    @Nonnull
    private final String errorMessage;

    private int lineNumber;

    private int columnNumber;

    public PostedAxiomsLoadFailureResponse(@Nonnull Response.Status status,
                                           @Nonnull String errorMessage,
                                           int lineNumber,
                                           int columnNumber) {
        this.status = checkNotNull(status);
        this.errorMessage = checkNotNull(errorMessage);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    /**
     * Gets the error messages associated with this failure response.  If the
     * load failure was caused by a parse exception then the error message will be
     * the parse exception.
     */
    @Nonnull
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the line number, if known, of a syntax error that caused the load failure.
     * @return The line number, if known, otherwise -1.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Gets the column number, if known, of a syntax error that caused the load failure.
     * @return The column number, if known, otherwise -1.
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> axioms() {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public Response toResponse() {
        return Response.status(status).entity(this).build();
    }
}

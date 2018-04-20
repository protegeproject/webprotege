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
public class PostedAxiomsLoadSuccessResponse implements PostedAxiomsLoadResponse {

    private int parsedAxiomsCount;

    private int addedAxioms;

    private Stream<OWLAxiom> axioms;

    public PostedAxiomsLoadSuccessResponse(int parsedAxiomsCount,
                                           @Nonnull Stream<OWLAxiom> axioms) {
        this.parsedAxiomsCount = parsedAxiomsCount;
        this.axioms = checkNotNull(axioms);
    }

    public int getParsedAxiomsCount() {
        return parsedAxiomsCount;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> axioms() {
        return axioms;
    }

    @Nonnull
    @Override
    public Response toResponse() {
        return Response.ok().entity(this).build();
    }
}

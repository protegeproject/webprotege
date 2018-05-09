package edu.stanford.bmir.protege.web.server.api.axioms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 */
public interface PostedAxiomsLoadResponse {

    /**
     * Determines whether or not the axiom loading was successful
     * @return true if the axiom loading was successful, otherwise false
     */
    boolean isSuccess();

    /**
     * Gets a stream of the axioms that were successfully loaded.  If the load failed then this
     * stream will be empty
     */
    @Nonnull
    @JsonIgnore
    Stream<OWLAxiom> axioms();

    @Nonnull
    @JsonIgnore
    Response toResponse();
}

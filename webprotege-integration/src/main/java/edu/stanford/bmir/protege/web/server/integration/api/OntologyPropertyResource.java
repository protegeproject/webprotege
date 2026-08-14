package edu.stanford.bmir.protege.web.server.integration.api;

import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyKind;
import edu.stanford.bmir.protege.web.server.integration.dto.OntologyPropertyListResponse;
import edu.stanford.bmir.protege.web.server.integration.service.OntologyPropertyService;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * Read-only REST API for OWL object, data, and annotation property frames.
 *
 * <p>Base path (under {@code /data/*}):
 * {@code /integration/projects/{projectId}/properties}</p>
 */
@Path("integration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OntologyPropertyResource {

    private static final String PROJECT_ID_PATTERN =
            "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Nonnull
    private final OntologyPropertyService service;

    @Inject
    public OntologyPropertyResource(@Nonnull OntologyPropertyService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    @GET
    @Path("projects/{projectId: " + PROJECT_ID_PATTERN + "}/properties")
    public Response getPropertyData(@PathParam("projectId") String projectId,
                                    @QueryParam("propertyIri") String propertyIri,
                                    @QueryParam("kind") String kind,
                                    @Context UserId userId) {
        try {
            if (userId == null || userId.isGuest()) {
                return Response.status(UNAUTHORIZED).entity(error("Authentication required")).build();
            }
            OntologyPropertyKind parsedKind = OntologyPropertyKind.fromQuery(kind);
            String resolvedIri = normalizeIri(propertyIri);
            if (resolvedIri != null && parsedKind == null) {
                return Response.status(BAD_REQUEST)
                               .entity(error("Query parameter 'kind' is required when 'propertyIri' is provided"))
                               .build();
            }
            if (resolvedIri == null) {
                return Response.ok(new OntologyPropertyListResponse(
                        service.listPropertyData(projectId, parsedKind, userId))).build();
            }
            return Response.ok(service.getPropertyData(projectId, resolvedIri, parsedKind, userId)).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(BAD_REQUEST).entity(error(e.getMessage())).build();
        }
    }

    private static String normalizeIri(String iri) {
        if (iri == null) {
            return null;
        }
        String trimmed = iri.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Nonnull
    private static ErrorBody error(@Nonnull String message) {
        return new ErrorBody(message);
    }

    public static class ErrorBody {

        private final String message;

        public ErrorBody(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

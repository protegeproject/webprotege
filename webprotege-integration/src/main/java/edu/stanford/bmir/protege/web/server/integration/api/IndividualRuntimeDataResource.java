package edu.stanford.bmir.protege.web.server.integration.api;

import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeData;
import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeDataListResponse;
import edu.stanford.bmir.protege.web.server.integration.dto.IndividualRuntimeDataRequest;
import edu.stanford.bmir.protege.web.server.integration.service.IndividualRuntimeDataService;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * RESTful API that reads/writes named-individual property values in the live ontology.
 *
 * <p>Base path (under {@code /data/*}):
 * {@code /integration/projects/{projectId}/individuals/runtime-data}</p>
 */
@Path("integration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndividualRuntimeDataResource {

    private static final String PROJECT_ID_PATTERN =
            "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Nonnull
    private final IndividualRuntimeDataService service;

    @Inject
    public IndividualRuntimeDataResource(@Nonnull IndividualRuntimeDataService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    @GET
    @Path("projects/{projectId: " + PROJECT_ID_PATTERN + "}/individuals/runtime-data")
    public Response getRuntimeData(@PathParam("projectId") String projectId,
                                   @QueryParam("individualIri") String individualIri,
                                   @Context UserId userId) {
        try {
            if (userId == null || userId.isGuest()) {
                return Response.status(UNAUTHORIZED).entity(error("Authentication required")).build();
            }
            if (individualIri == null || individualIri.trim().isEmpty()) {
                return Response.ok(new IndividualRuntimeDataListResponse(service.listRuntimeData(projectId, userId)))
                               .build();
            }
            IndividualRuntimeData data = service.getRuntimeData(projectId, individualIri, userId);
            return Response.ok(data).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(BAD_REQUEST).entity(error(e.getMessage())).build();
        }
    }

    @PUT
    @Path("projects/{projectId: " + PROJECT_ID_PATTERN + "}/individuals/runtime-data")
    public Response putRuntimeData(@PathParam("projectId") String projectId,
                                   @QueryParam("individualIri") String individualIri,
                                   @Context UserId userId,
                                   IndividualRuntimeDataRequest request) {
        try {
            if (userId == null || userId.isGuest()) {
                return Response.status(UNAUTHORIZED).entity(error("Authentication required")).build();
            }
            String resolvedIri = resolveIndividualIri(individualIri, request);
            IndividualRuntimeData saved = service.putRuntimeData(
                    projectId,
                    resolvedIri,
                    request == null ? new IndividualRuntimeDataRequest(resolvedIri, null) : request,
                    userId
            );
            return Response.ok(saved).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(BAD_REQUEST).entity(error(e.getMessage())).build();
        }
    }

    @PATCH
    @Path("projects/{projectId: " + PROJECT_ID_PATTERN + "}/individuals/runtime-data")
    public Response patchRuntimeData(@PathParam("projectId") String projectId,
                                     @QueryParam("individualIri") String individualIri,
                                     @Context UserId userId,
                                     IndividualRuntimeDataRequest request) {
        try {
            if (userId == null || userId.isGuest()) {
                return Response.status(UNAUTHORIZED).entity(error("Authentication required")).build();
            }
            if (request == null) {
                return Response.status(BAD_REQUEST).entity(error("Request body is required")).build();
            }
            String resolvedIri = resolveIndividualIri(individualIri, request);
            IndividualRuntimeData saved = service.patchRuntimeData(projectId, resolvedIri, request, userId);
            return Response.ok(saved).build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(BAD_REQUEST).entity(error(e.getMessage())).build();
        }
    }

    /**
     * Clears asserted property values on the ontology individual (types/sameAs retained).
     */
    @DELETE
    @Path("projects/{projectId: " + PROJECT_ID_PATTERN + "}/individuals/runtime-data")
    public Response deleteRuntimeData(@PathParam("projectId") String projectId,
                                      @QueryParam("individualIri") String individualIri,
                                      @Context UserId userId) {
        try {
            if (userId == null || userId.isGuest()) {
                return Response.status(UNAUTHORIZED).entity(error("Authentication required")).build();
            }
            if (individualIri == null || individualIri.trim().isEmpty()) {
                return Response.status(BAD_REQUEST)
                               .entity(error("Query parameter 'individualIri' is required"))
                               .build();
            }
            boolean deleted = service.deleteRuntimeData(projectId, individualIri, userId);
            if (!deleted) {
                return Response.status(NOT_FOUND)
                               .entity(error("No asserted property values to clear for individual: " + individualIri))
                               .build();
            }
            return Response.noContent().build();
        }
        catch (IllegalArgumentException e) {
            return Response.status(BAD_REQUEST).entity(error(e.getMessage())).build();
        }
    }

    /**
     * Resolves the target individual IRI from the query parameter and/or JSON body.
     *
     * <p>When both are present they must be equal after trim. Preferring the query value
     * without this check would let PUT/PATCH apply one individual's property payload to a
     * different individual named in the body (or vice versa).</p>
     */
    @Nonnull
    private static String resolveIndividualIri(String queryIri,
                                               IndividualRuntimeDataRequest request) {
        String fromQuery = normalizeIri(queryIri);
        String fromBody = request == null ? null : normalizeIri(request.getIndividualIri());
        if (fromQuery != null && fromBody != null && !fromQuery.equals(fromBody)) {
            throw new IllegalArgumentException(
                    "Query parameter 'individualIri' must match request body 'individualIri'");
        }
        if (fromQuery != null) {
            return fromQuery;
        }
        if (fromBody != null) {
            return fromBody;
        }
        throw new IllegalArgumentException(
                "individualIri must be provided as a query parameter or in the request body");
    }

    /** Returns a trimmed IRI, or {@code null} when missing/blank. */
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

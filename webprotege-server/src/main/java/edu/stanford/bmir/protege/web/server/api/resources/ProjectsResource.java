package edu.stanford.bmir.protege.web.server.api.resources;

import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.server.api.ApiRootResource;
import edu.stanford.bmir.protege.web.server.api.ResponseUtil;
import edu.stanford.bmir.protege.web.shared.api.ApiKey;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2018
 */
@Path("projects")
@Produces(MediaType.TEXT_PLAIN)
public class ProjectsResource implements ApiRootResource {

    @Nonnull
    private final ProjectResourceFactory projectResourceFactory;

    @Nonnull
    private final ActionExecutor executor;

    @Inject
    public ProjectsResource(@Nonnull ProjectResourceFactory projectResourceFactory,
                            @Nonnull ActionExecutor executor) {
        this.projectResourceFactory = checkNotNull(projectResourceFactory);
        this.executor = executor;
    }

    /**
     * Path to a specific project that is identified by the project ID (UUID)
     *
     * @param projectId The project id that is parsed from the path.
     * @return The {@link ProjectResource} for the project.
     */
    @Path("{projectId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public ProjectResource locateProjectResource(@PathParam("projectId") ProjectId projectId) {
        return projectResourceFactory.create(projectId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewProject(@Nonnull @Context UserId userId,
                                     @Nullable @Context ApiKey apiKey,
                                     @Nonnull @Context UriInfo uriInfo,
                                     @Nullable NewProjectSettings newProjectSettings) {
        // We require an API key for this particular operation
        if(apiKey == null) {
            return Response.status(UNAUTHORIZED)
                           .build();
        }
        if(newProjectSettings == null) {
            return ResponseUtil.badRequest("Missing new project details in body of POST");
        }
        CreateNewProjectAction action = new CreateNewProjectAction(newProjectSettings);
        CreateNewProjectResult result = executor.execute(action, userId);
        ProjectDetails projectDetails = result.getProjectDetails();
        // Respond with HTTP 201 (CREATED) and a location header that points
        // to the freshly created project.
        String projectId = projectDetails.getProjectId().getId();
        URI projectUri = uriInfo.getAbsolutePathBuilder()
                                .path(projectId)
                                .build();
        return Response.status(CREATED)
                       .entity(projectDetails)
                       .location(projectUri)
                       .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@Nonnull @Context UserId userId,
                                @Nonnull @Context UriInfo uriInfo) {
        GetAvailableProjectsResult result = executor.execute(new GetAvailableProjectsAction(),
                                                             userId);
        return Response.ok().entity(result.getDetails()).build();
    }
}

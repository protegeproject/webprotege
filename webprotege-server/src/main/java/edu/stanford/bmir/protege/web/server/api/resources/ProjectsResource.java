package edu.stanford.bmir.protege.web.server.api.resources;

import edu.stanford.bmir.protege.web.server.api.ApiRootResource;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.google.common.base.Preconditions.checkNotNull;

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

    @Inject
    public ProjectsResource(@Nonnull ProjectResourceFactory projectResourceFactory) {
        this.projectResourceFactory = checkNotNull(projectResourceFactory);
    }

    /**
     * Path to a specific project that is identified by the project ID (UUID)
     * @param projectId The project id that is parsed from the path.
     * @return The {@link ProjectResource} for the project.
     */
    @Path("{projectId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public ProjectResource locateProjectResource(@PathParam("projectId") ProjectId projectId) {
        return projectResourceFactory.create(projectId);
    }
}

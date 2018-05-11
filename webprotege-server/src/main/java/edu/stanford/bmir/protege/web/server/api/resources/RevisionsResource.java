package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.actions.GetRevisionAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.GetRevisionsAction;
import edu.stanford.bmir.protege.web.server.revision.RevisionDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 *
 * The changes within a context of a project
 */
public class RevisionsResource {

    private final ProjectId projectId;

    private final ActionExecutor executor;

    @Inject
    @AutoFactory
    public RevisionsResource(@Nonnull ProjectId projectId,
                             @Provided @Nonnull ActionExecutor executor) {
        this.projectId = checkNotNull(projectId);
        this.executor = checkNotNull(executor);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ImmutableList<RevisionDetails> listChanges(@Context UserId userId,
                                                      @QueryParam("from")
                                                      @DefaultValue("1")
                                                              RevisionNumber from,
                                                      @QueryParam("to")
                                                      @DefaultValue("HEAD")
                                                              RevisionNumber to,
                                                      @QueryParam("userId")
                                                              UserId author) {
        GetRevisionsAction action = new GetRevisionsAction(projectId,
                                                           from,
                                                           to,
                                                           author);
        return executor.execute(action, userId).getRevisions();
    }

    @GET
    @Path("/{revisionNumber : [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRevision(@Context UserId userId,
                                @Context UriInfo uriInfo,
                                @PathParam("revisionNumber") RevisionNumber revisionNumber) {
            Optional<RevisionDetails> revisionDetails = executor.execute(new GetRevisionAction(projectId, revisionNumber), userId)
                                                                .getRevisionDetails();
            if(revisionDetails.isPresent()) {
                return Response.ok(revisionDetails.get())
                               .build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND)
                               .location(uriInfo.getAbsolutePath())
                               .build();
            }
    }
}

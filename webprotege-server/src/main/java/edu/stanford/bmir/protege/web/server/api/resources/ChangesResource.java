package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.actions.GetRevisionsAction;
import edu.stanford.bmir.protege.web.server.revision.RevisionDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 *
 * The changes within a context of a project
 */
public class ChangesResource {

    private final ProjectId projectId;

    private final ActionExecutor executor;

    @Inject
    @AutoFactory
    public ChangesResource(@Nonnull ProjectId projectId,
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
//                                                      @Size(min = 1)
//                                                      @Pattern(regexp = "\\d+")
                                                              RevisionNumber from,
                                                      @QueryParam("to")
                                                      @DefaultValue("HEAD")
//                                                      @Size(min = 1)
//                                                      @Pattern(regexp = "\\d+|HEAD")
                                                              RevisionNumber to) {
        System.out.println("FROM " + from);
        System.out.println("TO " + to);
        return executor.execute(new GetRevisionsAction(projectId, from, to), userId).getRevisions();
    }
}

package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.AllProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActionExecutor actionExecutor;

    @AutoFactory
    public ProjectSettingsResource(@Nonnull ProjectId projectId,
                                   @Provided @Nonnull ActionExecutor actionExecutor) {
        this.projectId = checkNotNull(projectId);
        this.actionExecutor = checkNotNull(actionExecutor);
    }

    @Path("/")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getProjectSettings(@Context UserId userId) {
        var projectSettingsResult = actionExecutor.execute(new GetProjectSettingsAction(projectId),
                               userId);
        var entityCreationSettingsResult = actionExecutor.execute(new GetEntityCrudKitSettingsAction(projectId),
                                                           userId);
        var prefixDeclarationSettingsResult = actionExecutor.execute(new GetProjectPrefixDeclarationsAction(projectId),
                                                               userId);

        var tagsResult = actionExecutor.execute(new GetProjectTagsAction(projectId),
                                          userId);
        var projectSettings = AllProjectSettings.get(projectSettingsResult.getProjectSettings(),
                                                     entityCreationSettingsResult.getSettings(),
                                                     ImmutableList.copyOf(prefixDeclarationSettingsResult.getPrefixDeclarations()),
                                                     ImmutableList.copyOf(tagsResult.getTags()));
        return Response.ok(projectSettings).build();
    }

    @Path("/")
    @POST
    @Consumes(APPLICATION_JSON)
    public Response setProjectSettings(@Context UserId userId,
                                   AllProjectSettings allProjectSettings) {
        System.out.println(allProjectSettings);
        return Response.ok().build();
    }
}

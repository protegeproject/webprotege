package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.IRIPrefixUpdateStrategy;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.SetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.AllProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.SetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.TagData;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
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
    public ProjectSettingsResource(@Nonnull ProjectId projectId, @Provided @Nonnull ActionExecutor actionExecutor) {
        this.projectId = checkNotNull(projectId);
        this.actionExecutor = checkNotNull(actionExecutor);
    }

    @Path("/")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getProjectSettings(@Context UserId userId) {
        var projectSettingsResult = actionExecutor.execute(new GetProjectSettingsAction(projectId), userId);
        var entityCreationSettingsResult = actionExecutor.execute(new GetEntityCrudKitSettingsAction(projectId),
                                                                  userId);
        var prefixDeclarationSettingsResult = actionExecutor.execute(new GetProjectPrefixDeclarationsAction(projectId),
                                                                     userId);

        var tagsResult = actionExecutor.execute(new GetProjectTagsAction(projectId), userId);
        var projectSettings = AllProjectSettings.get(projectSettingsResult.getProjectSettings(),
                                                     entityCreationSettingsResult.getSettings(),
                                                     ImmutableList.copyOf(prefixDeclarationSettingsResult.getPrefixDeclarations()),
                                                     ImmutableList.copyOf(tagsResult.getTags()));
        var nilledOutProjectSettings = projectSettings.withProjectId(ProjectId.getNil());
        return Response.ok(nilledOutProjectSettings).build();
    }

    @Path("/")
    @POST
    @Consumes(APPLICATION_JSON)
    public Response setProjectSettings(@Context UserId userId, AllProjectSettings allProjectSettings) {
        var projectSettings = allProjectSettings.getProjectSettings().withProjectId(projectId);
        actionExecutor.execute(new SetProjectSettingsAction(projectSettings), userId);

        var entityCreationSettings = allProjectSettings.getEntityCreationSettings();
        var currentSettingsResult = actionExecutor.execute(new GetEntityCrudKitSettingsAction(projectId), userId);
        actionExecutor.execute(new SetEntityCrudKitSettingsAction(projectId,
                                                                  currentSettingsResult.getSettings(),
                                                                  entityCreationSettings,
                                                                  IRIPrefixUpdateStrategy.LEAVE_INTACT), userId);
        var prefixDeclarations = allProjectSettings.getPrefixDeclarations();
        actionExecutor.execute(new SetProjectPrefixDeclarationsAction(projectId, prefixDeclarations), userId);

        var projectTags = allProjectSettings.getProjectTags();
        var projectTagsData = projectTags.stream()
                                         .map(tag -> tag.withProjectId(projectId))
                                         .map(tag -> TagData.get(tag.getTagId(),
                                                                 tag.getLabel(),
                                                                 tag.getDescription(),
                                                                 tag.getColor(),
                                                                 tag.getBackgroundColor(),
                                                                 tag.getCriteria(),
                                                                 0))
                                         .collect(toImmutableList());
        actionExecutor.execute(new SetProjectTagsAction(projectId, projectTagsData), userId);
        return Response.ok().build();
    }
}

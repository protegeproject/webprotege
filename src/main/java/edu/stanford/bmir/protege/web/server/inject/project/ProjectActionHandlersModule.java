package edu.stanford.bmir.protege.web.server.inject.project;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.change.GetProjectChangesActionHandler;
import edu.stanford.bmir.protege.web.server.change.GetWatchedEntityChangesActionHandler;
import edu.stanford.bmir.protege.web.server.change.RevertRevisionActionHandler;
import edu.stanford.bmir.protege.web.server.collection.CreateCollectionItemsActionHandler;
import edu.stanford.bmir.protege.web.server.collection.GetCollectionItemsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.csv.ImportCSVFileActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.entity.GetDeprecatedEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.entity.LookupEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.form.GetFormDescriptorActionHander;
import edu.stanford.bmir.protege.web.server.form.SetFormDataActionHandler;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.issues.*;
import edu.stanford.bmir.protege.web.server.mansyntax.render.GetEntityRenderingActionHandler;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeActionHandler;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectActionHandler;
import edu.stanford.bmir.protege.web.server.metrics.GetMetricsActionHandler;
import edu.stanford.bmir.protege.web.server.obo.*;
import edu.stanford.bmir.protege.web.server.perspective.*;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityDataActionHandler;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberActionHandler;
import edu.stanford.bmir.protege.web.server.revision.GetRevisionSummariesActionHandler;
import edu.stanford.bmir.protege.web.server.search.PerformEntitySearchActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.GetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.SetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.usage.GetUsageActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesActionHandler;
import edu.stanford.bmir.protege.web.shared.collection.CreateCollectionItemsAction;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentAction;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentResult;

import static dagger.Provides.Type.SET;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
@Module
public class ProjectActionHandlersModule {

    @Provides(type = SET)
    public ProjectActionHandler provideGetProjectSettingsActionHandler(GetProjectSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetProjectSettingsActionHandler(SetProjectSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideUpdateClassFrameActionHandler(UpdateClassFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetObjectPropertyFrameActionHandler(GetObjectPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideUpdateObjectPropertyFrameHandler(UpdateObjectPropertyFrameHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideUpdateDataPropertyFrameHandler(UpdateDataPropertyFrameHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetAnnotationPropertyFrameActionHandler(
            GetAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideUpdateAnnotationPropertyFrameActionHandler(
            UpdateAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetNamedIndividualFrameActionHandler(
            GetNamedIndividualFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideUpdateNamedIndividualFrameHandler(UpdateNamedIndividualFrameHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetOntologyFramesActionHandler(GetOntologyFramesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetRootOntologyIdActionHandler(GetRootOntologyIdActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetOntologyAnnotationsActionHandler(
            GetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetOntologyAnnotationsActionHandler(
            SetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideDeleteEntityActionHandler(DeleteEntityActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateClassActionHandler(CreateClassActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateClassesActionHandler(CreateClassesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateObjectPropertyActionHandler(CreateObjectPropertyActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateDataPropertiesActionHandler(CreateDataPropertiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateAnnotationPropertiesActionHandler(
            CreateAnnotationPropertiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateNamedIndividualsActionHandler(
            CreateNamedIndividualsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideLookupEntitiesActionHandler(LookupEntitiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideAddWatchActionHandler(AddWatchActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideRemoveWatchActionHandler(RemoveWatchActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetEntityWatchesActionHandler(SetEntityWatchesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetWatchesActionHandler(GetWatchesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideImportCSVFileActionHandler(ImportCSVFileActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetUsageActionHandler(GetUsageActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetIndividualsActionHandler(GetIndividualsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetEntityRenderingActionHandler(GetEntityRenderingActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetEntityDataActionHandler(GetEntityDataActionHandler handler) {
        return handler;
    }


    @Provides(type = SET)
    public ProjectActionHandler provideGetDataPropertyFrameActionHandler(GetDataPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetMetricsActionHandler(GetMetricsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetEntityCrudKitSettingsActionHandler(
            SetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetEntityCrudKitSettingsActionHandler(
            GetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetManchesterSyntaxFrameActionHandler(
            GetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetManchesterSyntaxFrameActionHandler(
            SetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCheckManchesterSyntaxFrameActionHandler(
            CheckManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetManchesterSyntaxFrameCompletionsActionHandler(
            GetManchesterSyntaxFrameCompletionsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetProjectSharingSettingsActionHandler(
            GetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetProjectSharingSettingsActionHandler(
            SetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetHeadRevisionNumberActionHandler(
            GetHeadRevisionNumberActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetRevisionSummariesActionHandler(GetRevisionSummariesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideComputeProjectMergeActionHandler(ComputeProjectMergeActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideMergeUploadedProjectActionHandler(MergeUploadedProjectActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetProjectChangesActionHandler(GetProjectChangesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetWatchedEntityChangesActionHandler(
            GetWatchedEntityChangesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideRevertRevisionActionHandler(RevertRevisionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetPerspectiveLayoutActionHandler(GetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetPerspectiveLayoutActionHandler(SetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetPerspectivesActionHandler(GetPerspectivesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetPerspectivesActionHandler(SetPerspectivesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetFormDescriptorActionHander(GetFormDescriptorActionHander handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetIssuesActionHandler(GetIssuesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetDiscussionThreadsActionHandler(GetEntityDiscussionThreadsHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideCreateEntityDiscussionThreadActionHandler(CreateEntityDiscussionThreadHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideAddEntityCommentActionHandler(AddEntityCommentHandler handler) {
        return handler;
    }


    @Provides(type = SET)
    public ProjectActionHandler<DeleteEntityCommentAction, DeleteEntityCommentResult> provideDeleteEntityCommentActionHandler(DeleteEntityCommentHandler handler) {
        return handler;
    }


    @Provides(type = SET)
    public ProjectActionHandler provideEditCommentAction(EditCommentActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideSetDiscussionThreadStatusHandler(SetDiscussionThreadStatusHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideGetCommentedEntitiesActionHandler(GetCommentedEntitiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler provideResetPerspectiveLayoutActionHandler(ResetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesPerformEntitySearchActionHandler(PerformEntitySearchActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesDeleteEntitiesActionHandler(DeleteEntitiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetDeprecatedEntitiesActionHandler(GetDeprecatedEntitiesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetClassFrameActionHandler(GetClassFrameActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetOboTermIdActionHandler(GetOboTermIdActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetOboTermDefinitionActionHandler(GetOboTermDefinitionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetOboNamespacesActionHandler(GetOboNamespacesActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesSetOboTermIdActionHandler(SetOboTermIdActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesSetOboTermDefinitionActionHandler(SetOboTermDefinitionActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetOboTermRelationshipsActionHandler(GetOboTermRelationshipsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetOboTermCrossProductsActionHandler(GetOboTermCrossProductsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesSetOboTermCrossProductsActionHandler(SetOboTermCrossProductsActionHandler handler) {
        return handler;
    }


    @Provides(type = SET)
    public ProjectActionHandler providesGetOboTermSynonymsActionHandler(GetOboTermSynonymsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesSetOboTermSynonymsActionHandler(SetOboTermSynonymsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetOboTermXRefsActionHandler(GetOboTermXRefsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesSetOboTermXRefsActionHandler(SetOboTermXRefsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesSetFormDataHandler(SetFormDataActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesGetCollectionElementsActionHandler(GetCollectionItemsActionHandler handler) {
        return handler;
    }

    @Provides(type = SET)
    public ProjectActionHandler providesCreateCollectionItemsActionHandler(CreateCollectionItemsActionHandler handler) {
        return handler;
    }
}

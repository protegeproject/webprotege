package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.server.bulkop.EditAnnotationValuesActionHandler;
import edu.stanford.bmir.protege.web.server.bulkop.MoveToParentActionHandler;
import edu.stanford.bmir.protege.web.server.bulkop.SetAnnotationValueActionHandler;
import edu.stanford.bmir.protege.web.server.change.GetProjectChangesActionHandler;
import edu.stanford.bmir.protege.web.server.change.GetWatchedEntityChangesActionHandler;
import edu.stanford.bmir.protege.web.server.change.RevertRevisionActionHandler;
import edu.stanford.bmir.protege.web.server.collection.CreateCollectionItemsActionHandler;
import edu.stanford.bmir.protege.web.server.collection.GetCollectionItemsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsActionHandler;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.csv.ImportCSVFileActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.handlers.*;
import edu.stanford.bmir.protege.web.server.entity.GetDeprecatedEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.entity.LookupEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.entity.MergeEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.form.*;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsActionHandler;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsPageContainingIndividualActionHandler;
import edu.stanford.bmir.protege.web.server.issues.*;
import edu.stanford.bmir.protege.web.server.lang.GetProjectLangTagsActionHandler;
import edu.stanford.bmir.protege.web.server.mansyntax.render.GetEntityHtmlRenderingActionHandler;
import edu.stanford.bmir.protege.web.server.mansyntax.render.GetEntityRenderingActionHandler;
import edu.stanford.bmir.protege.web.server.match.GetMatchingEntitiesActionHandler;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeActionHandler;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectActionHandler;
import edu.stanford.bmir.protege.web.server.merge_add.ExistingOntologyMergeAddActionHandler;
import edu.stanford.bmir.protege.web.server.merge_add.NewOntologyMergeAddActionHandler;
import edu.stanford.bmir.protege.web.server.merge_add.GetAllOntologiesActionHandler;
import edu.stanford.bmir.protege.web.server.obo.*;
import edu.stanford.bmir.protege.web.server.perspective.*;
import edu.stanford.bmir.protege.web.server.project.GetProjectInfoActionHandler;
import edu.stanford.bmir.protege.web.server.project.GetProjectPrefixDeclarationsActionHandler;
import edu.stanford.bmir.protege.web.server.project.SetProjectPrefixDeclarationsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberActionHandler;
import edu.stanford.bmir.protege.web.server.revision.GetRevisionSummariesActionHandler;
import edu.stanford.bmir.protege.web.server.search.GetSearchSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.search.PerformEntitySearchActionHandler;
import edu.stanford.bmir.protege.web.server.search.SetSearchSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.GetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.sharing.SetProjectSharingSettingsActionHandler;
import edu.stanford.bmir.protege.web.server.tag.GetEntityTagsActionHandler;
import edu.stanford.bmir.protege.web.server.tag.GetProjectTagsActionHandler;
import edu.stanford.bmir.protege.web.server.tag.SetProjectTagsActionHandler;
import edu.stanford.bmir.protege.web.server.tag.UpdateEntityTagsActionHandler;
import edu.stanford.bmir.protege.web.server.usage.GetUsageActionHandler;
import edu.stanford.bmir.protege.web.server.viz.GetEntityGraphActionHandler;
import edu.stanford.bmir.protege.web.server.viz.GetUserProjectEntityGraphCriteriaActionHandler;
import edu.stanford.bmir.protege.web.server.viz.SetEntityGraphActiveFiltersActionHandler;
import edu.stanford.bmir.protege.web.server.viz.SetUserProjectEntityGraphCriteriaActionHandler;
import edu.stanford.bmir.protege.web.server.watches.AddWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesActionHandler;
import edu.stanford.bmir.protege.web.server.watches.RemoveWatchActionHandler;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesActionHandler;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentAction;
import edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveDetailsAction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
@Module
public class ProjectActionHandlersModule {

    @Provides @IntoSet
    public ProjectActionHandler provideGetProjectSettingsActionHandler(GetProjectSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetProjectSettingsActionHandler(SetProjectSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideUpdateClassFrameActionHandler(UpdateClassFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetObjectPropertyFrameActionHandler(GetObjectPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideUpdateObjectPropertyFrameHandler(UpdateObjectPropertyFrameHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideUpdateDataPropertyFrameHandler(UpdateDataPropertyFrameHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetAnnotationPropertyFrameActionHandler(
            GetAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideUpdateAnnotationPropertyFrameActionHandler(
            UpdateAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetNamedIndividualFrameActionHandler(
            GetNamedIndividualFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetAllOntologiesActionHandler(GetAllOntologiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideUpdateNamedIndividualFrameHandler(UpdateNamedIndividualFrameHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetOntologyFramesActionHandler(GetOntologyFramesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetRootOntologyIdActionHandler(GetRootOntologyIdActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetOntologyAnnotationsActionHandler(
            GetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetOntologyAnnotationsActionHandler(
            SetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCreateClassesActionHandler(CreateClassesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCreateObjectPropertyActionHandler(CreateObjectPropertiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCreateDataPropertiesActionHandler(CreateDataPropertiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCreateAnnotationPropertiesActionHandler(
            CreateAnnotationPropertiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCreateNamedIndividualsActionHandler(
            CreateNamedIndividualsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideLookupEntitiesActionHandler(LookupEntitiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideAddWatchActionHandler(AddWatchActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideRemoveWatchActionHandler(RemoveWatchActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetEntityWatchesActionHandler(SetEntityWatchesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetWatchesActionHandler(GetWatchesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideImportCSVFileActionHandler(ImportCSVFileActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetUsageActionHandler(GetUsageActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetIndividualsActionHandler(GetIndividualsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetEntityRenderingActionHandler(GetEntityRenderingActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetDataPropertyFrameActionHandler(GetDataPropertyFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetEntityCrudKitSettingsActionHandler(
            SetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetEntityCrudKitSettingsActionHandler(
            GetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetManchesterSyntaxFrameActionHandler(
            GetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetManchesterSyntaxFrameActionHandler(
            SetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCheckManchesterSyntaxFrameActionHandler(
            CheckManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetManchesterSyntaxFrameCompletionsActionHandler(
            GetManchesterSyntaxFrameCompletionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetProjectSharingSettingsActionHandler(
            GetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetProjectSharingSettingsActionHandler(
            SetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetHeadRevisionNumberActionHandler(
            GetHeadRevisionNumberActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetRevisionSummariesActionHandler(GetRevisionSummariesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideComputeProjectMergeActionHandler(ComputeProjectMergeActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideNewOntologyMergeAddActionHandler(NewOntologyMergeAddActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideMergeUploadedProjectActionHandler(MergeUploadedProjectActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetProjectChangesActionHandler(GetProjectChangesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetWatchedEntityChangesActionHandler(
            GetWatchedEntityChangesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideRevertRevisionActionHandler(RevertRevisionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetPerspectiveLayoutActionHandler(GetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetPerspectiveLayoutActionHandler(SetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetPerspectivesActionHandler(GetPerspectivesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetPerspectivesActionHandler(SetPerspectivesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetFormDescriptorActionHander(GetFormDescriptorActionHander handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetIssuesActionHandler(GetIssuesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetDiscussionThreadsActionHandler(GetEntityDiscussionThreadsHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCreateEntityDiscussionThreadActionHandler(CreateEntityDiscussionThreadHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideAddEntityCommentActionHandler(AddEntityCommentHandler handler) {
        return handler;
    }


    @Provides @IntoSet
    public ProjectActionHandler<DeleteEntityCommentAction, DeleteEntityCommentResult> provideDeleteEntityCommentActionHandler(DeleteEntityCommentHandler handler) {
        return handler;
    }


    @Provides @IntoSet
    public ProjectActionHandler provideEditCommentAction(EditCommentActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetDiscussionThreadStatusHandler(SetDiscussionThreadStatusHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetCommentedEntitiesActionHandler(GetCommentedEntitiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideResetPerspectiveLayoutActionHandler(ResetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesPerformEntitySearchActionHandler(PerformEntitySearchActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesDeleteEntitiesActionHandler(DeleteEntitiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetDeprecatedEntitiesActionHandler(GetDeprecatedEntitiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetClassFrameActionHandler(GetClassFrameActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetOboTermIdActionHandler(GetOboTermIdActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetOboTermDefinitionActionHandler(GetOboTermDefinitionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetOboNamespacesActionHandler(GetOboNamespacesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetOboTermIdActionHandler(SetOboTermIdActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetOboTermDefinitionActionHandler(SetOboTermDefinitionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetOboTermRelationshipsActionHandler(GetOboTermRelationshipsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetOboTermCrossProductsActionHandler(GetOboTermCrossProductsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetOboTermCrossProductsActionHandler(SetOboTermCrossProductsActionHandler handler) {
        return handler;
    }


    @Provides @IntoSet
    public ProjectActionHandler providesGetOboTermSynonymsActionHandler(GetOboTermSynonymsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetOboTermSynonymsActionHandler(SetOboTermSynonymsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetOboTermXRefsActionHandler(GetOboTermXRefsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetOboTermXRefsActionHandler(SetOboTermXRefsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetFormDataHandler(SetFormDataActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetCollectionElementsActionHandler(GetCollectionItemsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesCreateCollectionItemsActionHandler(CreateCollectionItemsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetClassHierarchyChildrenActionHandler(GetEntityHierarchyChildrenActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetHierarchyPathsToRootActionHandler(GetHierarchyPathsToRootActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetHierarchyRootsActionHandler(GetHierarchyRootsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesMoveHierarchyNodeActionHandler(MoveHierarchyNodeActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetProjectPrefixDeclarationsActionHandler(GetProjectPrefixDeclarationsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetProjectPrefixDeclarationsActionHandler(SetProjectPrefixDeclarationsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesMergeClassesActionHandler(MergeEntitiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetEntityTagsActionHandler(GetEntityTagsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesUpdateEntityTagsActionHandler(UpdateEntityTagsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetProjectTagsActionHandler(GetProjectTagsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetProjectTagsActionHandler(SetProjectTagsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesAddAxiomActionHandler(AddAxiomsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesDeleteAxiomActionHandler(DeleteAxiomsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetRevisionsActionHandler(GetRevisionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetRevisionActionHandler(GetRevisionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetMatchingEntitiesAction(GetMatchingEntitiesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetProjectInfoActionHandler(GetProjectInfoActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetHierarchySiblingsActionHandler(GetHierarchySiblingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetIndividualsPageContainingIndividualActionHandler(GetIndividualsPageContainingIndividualActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetAnnotationValueActionHandler(SetAnnotationValueActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideReplaceAnnotationValuesActionHandler(EditAnnotationValuesActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideMoveToParentActionHandler(MoveToParentActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetEntityDotRenderingActionHandler(GetEntityGraphActionHandler handler) {
        return handler;
    }


    @Provides @IntoSet
    public ProjectActionHandler providesGetEntityCrudKitsActionHandler(GetEntityCrudKitsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetEntityFormDataActionHandler(SetEntityFormsDataActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetUserProjectEntityGraphCriteriaActionHandler(
            GetUserProjectEntityGraphCriteriaActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetEntityFormActionHandler(GetEntityFormActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetUserProjectEntityGraphCriteriaActionHandler(
            SetUserProjectEntityGraphCriteriaActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesGetProjectFormDescriptorsActionHandler(GetProjectFormDescriptorsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler providesSetProjectFormDescriptorsActionHandler(SetProjectFormDescriptorsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetEntityGraphActiveFiltersActionHandler(SetEntityGraphActiveFiltersActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetEntityFormDescriptorActionHandler(GetEntityFormDescriptorActionHandler actionHandler) {
        return actionHandler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetEntityFormDescriptorActionHandler(SetEntityFormDescriptorActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetFreshFormIdActionHandler(GetFreshFormIdActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetEntityHtmlRenderingActionHandler(GetEntityHtmlRenderingActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideCopyFormDescriptorsFromProjectActionHandler(CopyFormDescriptorsFromProjectActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideDeleteFormActionHandler(DeleteFormActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetProjectLangTagsActionHandler(GetProjectLangTagsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetSearchSettingsActionHandler(GetSearchSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideSetSearchSettingsActionHandler(SetSearchSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideUpdateFormDescriptorActionHandler(UpdateFormDescriptorActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideGetPerspectiveDetailsActionHandler(GetPerspectiveDetailsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ProjectActionHandler provideResetPerspectivesActionHandler(ResetPerspectivesActionHandler handler) {
        return handler;
    }
    public ProjectActionHandler providesExistingOntologyMergeAddActionHandler(ExistingOntologyMergeAddActionHandler handler){
        return handler;
    }
}

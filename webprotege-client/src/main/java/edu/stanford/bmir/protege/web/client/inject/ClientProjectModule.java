package edu.stanford.bmir.protege.web.client.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.client.app.*;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsEditor;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsEditorImpl;
import edu.stanford.bmir.protege.web.client.crud.obo.UserIdRangeEditor;
import edu.stanford.bmir.protege.web.client.crud.obo.UserIdRangeEditorImpl;
import edu.stanford.bmir.protege.web.client.editor.EditorManagerSelector;
import edu.stanford.bmir.protege.web.client.editor.EntityManagerSelectorImpl;
import edu.stanford.bmir.protege.web.client.entity.CreateEntitiesDialogViewImpl;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityDialogView;
import edu.stanford.bmir.protege.web.client.form.FormView;
import edu.stanford.bmir.protege.web.client.form.FormViewImpl;
import edu.stanford.bmir.protege.web.client.frame.ManchesterSyntaxFrameEditor;
import edu.stanford.bmir.protege.web.client.frame.ManchesterSyntaxFrameEditorImpl;
import edu.stanford.bmir.protege.web.client.hierarchy.PropertyHierarchyPortletView;
import edu.stanford.bmir.protege.web.client.hierarchy.PropertyHierarchyPortletViewImpl;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListView;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListViewImpl;
import edu.stanford.bmir.protege.web.client.lang.LangCodesProvider;
import edu.stanford.bmir.protege.web.client.lang.LanguageCodes;
import edu.stanford.bmir.protege.web.client.match.*;
import edu.stanford.bmir.protege.web.client.ontology.annotations.AnnotationsView;
import edu.stanford.bmir.protege.web.client.ontology.annotations.AnnotationsViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionCheckerImpl;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveLinkManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveLinkManagerImpl;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactory;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactoryGenerated;
import edu.stanford.bmir.protege.web.client.portlet.PortletModulesGenerated;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.progress.BusyViewImpl;
import edu.stanford.bmir.protege.web.client.project.*;
import edu.stanford.bmir.protege.web.client.projectsettings.*;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRendererImpl;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRenderer;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRendererImpl;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsView;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsViewImpl;
import edu.stanford.bmir.protege.web.client.tag.*;
import edu.stanford.bmir.protege.web.client.watches.WatchTypeSelectorView;
import edu.stanford.bmir.protege.web.client.watches.WatchTypeSelectorViewImpl;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Module(includes = PortletModulesGenerated.class)
public class ClientProjectModule {

    private final ProjectId projectId;

    public ClientProjectModule(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Provides
    ProjectId provideProjectId() {
        return projectId;
    }

    @Provides
    AnnotationsView provideAnnotationsView(AnnotationsViewImpl view) {
        return view;
    }

    @Provides
    ManchesterSyntaxFrameEditor provideManchesterSyntaxFrameEditor(ManchesterSyntaxFrameEditorImpl editor) {
        return editor;
    }

    @Provides
    ShowProjectDetailsHandler provideShowProjectDetailsHandler(ShowProjectDetailsHandlerImpl handler) {
        return handler;
    }

    @Provides
    UploadAndMergeHandler provideUploadAndMergeHandler(UploadAndMergeHandlerImpl handler) {
        return handler;
    }

    @Provides
    @ProjectSingleton
    ShowFreshEntitySettingsHandler provideShowFreshEntitySettingsHandler(ShowFreshEntitySettingsHandlerImpl handler) {
        return handler;
    }

    @Provides
    LoggedInUserProjectPermissionChecker provideLoggedInUserProjectPermissionChecker(LoggedInUserProjectPermissionCheckerImpl checker) {
        return checker;
    }

    @Provides
    EditorManagerSelector provideEditorManagerSelector(EntityManagerSelectorImpl selector) {
        return selector;
    }

    @Provides
    ChangeListView provideChangeListView(ChangeListViewImpl view) {
        return view;
    }

    @Provides
    SharingSettingsView provideSharingSettingsView(SharingSettingsViewImpl view) {
        return view;
    }

    @Provides
    IndividualsListView provideIndividualsListView(IndividualsListViewImpl view) {
        return view;
    }

    @Provides
    WatchTypeSelectorView provideWatchTypeSelectorView(WatchTypeSelectorViewImpl view) {
        return view;
    }

    @Provides
    FormView provideFormView(FormViewImpl view) {
        return view;
    }

    @Provides
    @LanguageCodes
    List<LanguageCode> provideLanguageCodes(LangCodesProvider provider) {
        return provider.get();
    }

    @Provides
    EntityCrudKitSettingsEditor provideEntityCrudKitSettingsEditor(EntityCrudKitSettingsEditorImpl editor) {
        return editor;
    }

    @Provides
    UserIdRangeEditor provideUserIdRangeEditor(UserIdRangeEditorImpl editor) {
        return editor;
    }


    @Provides
    PerspectiveLinkManager providePerspectiveLinkManager(PerspectiveLinkManagerImpl linkManager) {
        return linkManager;
    }


    @Provides
    @ProjectSingleton
    PortletFactory providePortletFactory(PortletFactoryGenerated portletFactoryGenerated) {
        return portletFactoryGenerated;
    }

    @Provides
    TreeWidget<EntityHierarchyNode, OWLEntity> providesEntityHierarchyTree() {
        return new TreeWidget<>();
    }

    @Provides
    PropertyHierarchyPortletView providePropertyHierarchyPortletView(PropertyHierarchyPortletViewImpl impl) {
        return impl;
    }

    @Provides
    CreateEntityDialogView providesCreateEntityDialogView(CreateEntitiesDialogViewImpl impl) {
        return impl;
    }

    @Provides
    BlankCriteriaView provideEntityIsDeprecatedCriteriaView(BlankCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    AnnotationCriteriaView provideEntityAnnotationCriteriaView(AnnotationCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    SelectableCriteriaTypeView provideSelectableCriteriaTypeView(SelectableCriteriaTypeViewImpl impl) {
        return impl;
    }

    @Provides
    SimpleStringCriteriaView provideSimpleStringCriteriaView(SimpleStringCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    NumericValueCriteriaView provideNumericValueCriteriaView(NumericValueCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    LangTagMatchesCriteriaView provideLangTagMatchesCriteriaView(LangTagMatchesCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    DateView provideDateTimeView(DateViewImpl impl) {
        return impl;
    }

    @Provides
    QueryPortletView provideMatchPortletView(QueryPortletViewImpl impl) {
        return impl;
    }

    @Provides
    CriteriaListView provideCriteriaListView(CriteriaListViewImpl impl) {
        return impl;
    }

    @Provides
    CriteriaListCriteriaViewContainer provideCriteriaListCriteriaViewContainer(CriteriaListViewViewContainerImpl impl) {
        return impl;
    }

    @Provides
    AnnotationPropertyCriteriaView provideAnnotationPropertyCriteriaView(AnnotationPropertyCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    EntityTypeCriteriaView provideEntityTypeCriteriaView(EntityTypeCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    IriEqualsView provideIriEqualsView(IriEqualsViewImpl impl) {
        return impl;
    }

    @Provides
    AnnotationPropertyPairView provideAnnotationPropertyPairView(AnnotationPropertyPairViewImpl impl) {
        return impl;
    }

    @Provides
    ClassSelectorView provideClassView(ClassSelectorViewImpl impl) {
        return impl;
    }

    @Provides
    TagCriteriaView provideTagCriteriaView(TagCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    TagCriteriaListView provideTagCriteriaListView(TagCriteriaListViewImpl impl) {
        return impl;
    }

    @Provides
    TagCriteriaViewContainer provideTagCriteriaViewContainer(TagCriteriaViewContainerImpl impl) {
        return impl;
    }

    @Provides
    AnnotationPropertyIriRenderer provideAnnotationPropertyIriRenderer(AnnotationPropertyIriRendererImpl impl) {
        return impl;
    }

    @Provides
    ClassIriRenderer provideClassIriRenderer(ClassIriRendererImpl impl) {
        return impl;
    }

    @Provides
    GeneralSettingsView provideGeneralSettingsView(GeneralSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    SlackWebhookSettingsView provideSlackWebhookSettingsView(SlackWebhookSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    WebhookSettingsView provideWebhookSettingsView(WebhookSettingsViewImpl impl) {
        return impl;
    }

}



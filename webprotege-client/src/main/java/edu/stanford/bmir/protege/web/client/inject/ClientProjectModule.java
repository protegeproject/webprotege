package edu.stanford.bmir.protege.web.client.inject;

import com.google.common.collect.ImmutableList;
import com.google.gwt.storage.client.Storage;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.client.bulkop.*;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.crud.*;
import edu.stanford.bmir.protege.web.client.crud.obo.OboIdSuffixSettingsView;
import edu.stanford.bmir.protege.web.client.crud.obo.OboIdSuffixSettingsViewImpl;
import edu.stanford.bmir.protege.web.client.crud.obo.UserIdRangeEditor;
import edu.stanford.bmir.protege.web.client.crud.obo.UserIdRangeEditorImpl;
import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedNameSuffixSettingsView;
import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedNameSuffixSettingsViewImpl;
import edu.stanford.bmir.protege.web.client.crud.uuid.UuidSuffixSettingViewImpl;
import edu.stanford.bmir.protege.web.client.crud.uuid.UuidSuffixSettingsView;
import edu.stanford.bmir.protege.web.client.editor.EditorManagerSelector;
import edu.stanford.bmir.protege.web.client.editor.EntityManagerSelectorImpl;
import edu.stanford.bmir.protege.web.client.entity.CreateEntitiesDialogViewImpl;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityDialogView;
import edu.stanford.bmir.protege.web.client.entity.MergeEntitiesView;
import edu.stanford.bmir.protege.web.client.entity.MergeEntitiesViewImpl;
import edu.stanford.bmir.protege.web.client.form.*;
import edu.stanford.bmir.protege.web.client.form.input.CheckBoxView;
import edu.stanford.bmir.protege.web.client.form.input.CheckBoxViewImpl;
import edu.stanford.bmir.protege.web.client.frame.ManchesterSyntaxFrameEditor;
import edu.stanford.bmir.protege.web.client.frame.ManchesterSyntaxFrameEditorImpl;
import edu.stanford.bmir.protege.web.client.hierarchy.*;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListView;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListViewImpl;
import edu.stanford.bmir.protege.web.client.lang.*;
import edu.stanford.bmir.protege.web.client.library.tokenfield.*;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupView;
import edu.stanford.bmir.protege.web.client.list.EntityNodeListPopupViewImpl;
import edu.stanford.bmir.protege.web.client.match.*;
import edu.stanford.bmir.protege.web.client.ontology.annotations.AnnotationsView;
import edu.stanford.bmir.protege.web.client.ontology.annotations.AnnotationsViewImpl;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionCheckerImpl;
import edu.stanford.bmir.protege.web.client.perspective.*;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactory;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactoryGenerated;
import edu.stanford.bmir.protege.web.client.portlet.PortletModulesGenerated;
import edu.stanford.bmir.protege.web.client.project.*;
import edu.stanford.bmir.protege.web.client.projectsettings.*;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRendererImpl;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRenderer;
import edu.stanford.bmir.protege.web.client.renderer.ClassIriRendererImpl;
import edu.stanford.bmir.protege.web.client.search.*;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsView;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsViewImpl;
import edu.stanford.bmir.protege.web.client.shortform.ShortFormModule;
import edu.stanford.bmir.protege.web.client.tag.*;
import edu.stanford.bmir.protege.web.client.viz.*;
import edu.stanford.bmir.protege.web.client.watches.WatchView;
import edu.stanford.bmir.protege.web.client.watches.WatchViewImpl;
import edu.stanford.bmir.protege.web.shared.crud.ConditionalIriPrefix;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.*;
import edu.stanford.protege.gwt.graphtree.client.MultiSelectionModel;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.form.FormControlStackRepeatingView.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Module(includes = {PortletModulesGenerated.class, ShortFormModule.class})
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
    UploadAndMergeAdditionsHandler provideUploadAndMergeAdditionsHandler(UploadAndMergeAdditionsHandlerImpl handler) {
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
    WatchView provideWatchTypeSelectorView(WatchViewImpl view) {
        return view;
    }

    @Provides
    FormView provideFormView(FormViewImpl view) {
        return view;
    }

    @Provides
    UserIdRangeEditor provideUserIdRangeEditor(UserIdRangeEditorImpl editor) {
        return editor;
    }


    @Provides
    ProjectPerspectivesService providePerspectiveLinkManager(ProjectPerspectivesServiceImpl linkManager) {
        return linkManager;
    }


    @Provides
    @ProjectSingleton
    PortletFactory providePortletFactory(PortletFactoryGenerated portletFactoryGenerated) {
        return portletFactoryGenerated;
    }

    @Provides
    TreeWidget<EntityNode, OWLEntity> providesEntityHierarchyTree() {
        MultiSelectionModel selectionModel = new MultiSelectionModel();
        return new TreeWidget<>(selectionModel);
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
    EntityRelationshipCriteriaView provideEntityRelationshipCriteriaView(EntityRelationshipCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    RelationValueThatIsEqualToView provideRelationValueThatIsEqualToView(RelationValueThatIsEqualToViewImpl impl) {
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

    @Provides
    DefaultDisplayNameSettingsView provideDisplayLanguagesView(DefaultDisplayNameSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    DefaultDictionaryLanguageView provideEntityDefaultLanguagesView(DefaultDictionaryLanguageViewImpl impl) {
        return impl;
    }

    @Provides
    DictionaryLanguageDataView provideDictionaryLanguageDataView(DictionaryLanguageDataViewImpl impl) {
        return impl;
    }

    @Provides
    DefaultLanguageTagView provideDefaultLanguageTagView(DefaultLanguageTagViewImpl impl) {
        return impl;
    }

    @Provides
    DisplayNameSettingsTopBarView providePreferredLanguageView(DisplayNameSettingsTopBarViewImpl impl) {
        return impl;
    }

    @Provides
    DisplayNameSettingsView provideDisplayLanguageEditorView(DisplayNameSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    Storage provideLocalStorage() {
        return Storage.getLocalStorageIfSupported();
    }

    @Provides
    LanguageUsageView provideLanguageUsageView(LanguageUsageViewImpl impl) {
        return impl;
    }

    @Provides
    HierarchyFieldView provideHierarchyFieldView(HierarchyFieldViewImpl impl) {
        return impl;
    }

    @Provides
    EntityNodeListPopupView provideEntityNodeListPopupView(EntityNodeListPopupViewImpl impl) {
        return impl;
    }

    @Provides
    HierarchyPopupView provideHierarchyPopupView(HierarchyPopupViewImpl impl) {
        return impl;
    }

    @Provides
    BulkEditOperationViewContainer provideBulkEditOperationViewContainer(BulkEditOperationViewContainerImpl impl) {
        return impl;
    }

    @Provides
    SetAnnotationValueView provideSetAnnotationValueView(SetAnnotationValueViewImpl impl) {
        return impl;
    }

    @Provides
    EditAnnotationsView provideReplaceAnnotationValuesView(EditAnnotationsViewImpl impl) {
        return impl;
    }

    @Provides
    MoveToParentView provideMoveToParentView(MoveToParentViewImpl impl) {
        return impl;
    }

    @Provides
    MergeEntitiesView provideMergeEntitiesView(MergeEntitiesViewImpl impl) {
        return impl;
    }

    @Provides
    AnnotationSimpleMatchingCriteriaView provideAnnotationSimpleMatchingCriteriaView(AnnotationSimpleMatchingCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    CommitMessageInputView provideCommitMessageInputView(CommitMessageInputViewImpl impl) {
        return impl;
    }

    @Provides
    VizView provideVizView(VizViewImpl view) {
        return view;
    }

    @Provides
    LargeGraphMessageView provideLargeGraphMessageView(LargeGraphMessageViewImpl impl) {
        return impl;
    }

    @Provides
    FormsManagerView provideFormsManagerView(FormsManagerViewImpl impl) {
        return impl;
    }

    @Provides
    RelationshipEdgePropertyEqualsCriteriaView provideRelationshipEdgePropertyEqualsCriteriaView(RelationshipEdgePropertyEqualsCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    FormFieldDescriptorView provideFormElementDescriptorEditorView(FormFieldDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    TextControlDescriptorView provideTextFieldDescriptorEditorView(TextControlDescriptorViewImpl view) {
        return view;
    }

    @Provides
    FormDescriptorView provideFormDescriptorView(FormDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    ObjectListView provideFormElementDescriptorListView(@Nonnull ObjectListViewImpl impl) {
        return impl;
    }

    @Provides
    ObjectListViewHolder provideFormElementDescriptorViewHolder(@Nonnull ObjectListViewHolderImpl impl) {
        return impl;
    }


    @Provides
    FormFieldView provideFormElementView(FormFieldViewImpl impl) {
        return impl;
    }

    @Provides
    FormViewRow provideFormViewRow(FormViewRowImpl impl) {
        return impl;
    }

    @Provides
    NoFormView provideNoFormView(NoFormViewImpl impl) {
        return impl;
    }

    @Provides
    NumberControlRangeView provideNumberFieldRangeView(@Nonnull NumberControlRangeViewImpl impl) {
        return impl;
    }

    @Provides
    NumberControlDescriptorView provideNumberFieldDescriptorView(@Nonnull NumberControlDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    ChoiceDescriptorView provideChoiceDescriptorView(@Nonnull ChoiceDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    SingleChoiceControlDescriptorView provideChoiceFieldDescriptorView(SingleChoiceControlDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    BlankEdgeCriteriaPresenter<AnyRelationshipEdgeCriteria> providAnyRelationshipEdgeCriteriaPresenter(BlankCriteriaView view) {
        return new BlankEdgeCriteriaPresenter<>(view);
    }

    @Provides
    BlankEdgeCriteriaPresenter<AnySubClassOfEdgeCriteria> providAnySubClassOfEdgeCriteriaPresenter(BlankCriteriaView view) {
        return new BlankEdgeCriteriaPresenter<>(view);
    }

    @Provides
    BlankEdgeCriteriaPresenter<AnyInstanceOfEdgeCriteria> providAnyInstanceOfEdgeCriteriaPresenter(BlankCriteriaView view) {
        return new BlankEdgeCriteriaPresenter<>(view);
    }

    @Provides
    BlankEdgeCriteriaPresenter<AnyEdgeCriteria> provideAnyEdgeCriteriaPresenter(BlankCriteriaView view) {
        return new BlankEdgeCriteriaPresenter<>(view);
    }

    @Provides
    EntityGraphSettingsView provideVizSettingsView(EntityGraphSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    LanguageMapEntryView provideLanguageMapEntryView(LanguageMapEntryViewImpl impl) {
        return impl;
    }

    @Provides
    EntityIsCriteriaView provideEntityIsCriteriaView(EntityIsCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    NoControlDescriptorView provideNoFieldDescriptorView(NoControlDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    EntityGraphFilterView provideEntityGraphFilterView(EntityGraphFilterViewImpl impl) {
        return impl;
    }

    @Provides
    ImageDescriptorView provideImageDescriptorView(ImageDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    EntityGraphFilterListItemView provideEntityGraphFilterListItemView(EntityGraphFilterListItemViewImpl impl) {
        return impl;
    }

    @Provides
    EntityNameControlDescriptorView provideEntityNameFieldDescriptorView(@Nonnull EntityNameControlDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    EntityGraphFilterListView provideEntityGraphFilterListView(EntityGraphFilterListViewImpl impl) {
        return impl;
    }

    @Provides
    SubFormControlDescriptorView provideSubFormFieldDescriptorView(@Nonnull SubFormControlDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    EntityGraphView provideEntityGraphView(EntityGraphViewImpl impl) {
        return impl;
    }

    @Provides
    NoFormDescriptorSelectedView provideNoFormDescriptorSelectedView(NoFormDescriptorSelectedViewImpl impl) {
        return impl;
    }

    @Provides
    GridView provideGridView(GridViewImpl impl) {
        return impl;
    }

    @Provides
    TokenFieldPresenter<FilterName> provideTokenFieldPresenter(TokenFieldView view,
                                                               TokenPresenterFactory presenterFactory) {
        return new TokenFieldPresenter<>(view, presenterFactory);
    }

    @Provides
    TokenView provideTokenView(TokenViewImpl impl) {
        return impl;
    }

    @Provides
    GridRowView provideGridRowView(GridRowViewImpl impl) {
        return impl;
    }

    @Provides
        TokenFieldView provideTokenFieldView(TokenFieldViewImpl impl) {
        return impl;
    }

    @Provides
    GridCellView provideGridCellView(GridCellViewImpl impl) {
        return impl;
    }

    @Provides
    GridControlDescriptorView provideGridFieldDescriptorView(GridControlDescriptorViewImpl impl) {
        return impl;
    }


    @Provides
    ObjectPresenter<FormFieldDescriptor> providesFormElementDescriptorPresenter(FormFieldDescriptorPresenter presenter) {
        return presenter;
    }

    @Provides
    ImmutableList<FormControlDescriptorPresenterFactory> provideFormFieldDescriptorPresenterFactories(
            TextControlDescriptorPresenterFactory textFieldDescriptorEditorPresenterFactory,
            NumberControlDescriptorPresenterFactory numberFieldDescriptorPresenterFactory,
            SingleChoiceControlDescriptorPresenterFactory choiceFieldDescriptorPresenterFactory,
            MultiChoiceControlDescriptorPresenterFactory multiChoiceControlDescriptorPresenterFactory,
            ImageDescriptorPresenterFactory imageDescriptorPresenterFactory,
            EntityNameControlDescriptorPresenterFactory entityNameFieldDescriptorPresenterFactory,
            SubFormControlDescriptorPresenterFactory subFormControlDescriptorPresenterFactory,
            GridControlDescriptorPresenterFactory gridControlDescriptorPresenterFactory) {
        return ImmutableList.of(textFieldDescriptorEditorPresenterFactory,
                                numberFieldDescriptorPresenterFactory,
                                choiceFieldDescriptorPresenterFactory,
                                multiChoiceControlDescriptorPresenterFactory,
                                imageDescriptorPresenterFactory,
                                entityNameFieldDescriptorPresenterFactory,
                                subFormControlDescriptorPresenterFactory,
                                gridControlDescriptorPresenterFactory);
    }

    @Provides
    FormControlDescriptorChooserView providesFormFieldDescriptorChooserView(FormControlDescriptorChooserViewImpl impl) {
        return impl;
    }

    @Provides
    ObjectPresenter<GridColumnDescriptor> provideGridColumnDescriptor(GridColumnDescriptorPresenter presenter) {
        return presenter;
    }

    @Provides
    GridColumnDescriptorView provideGridColumnDescriptorView(GridColumnDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    OwlBindingView provideOwlBindingView(OwlBindingViewImpl impl) {
        return impl;
    }

    @Provides
    GridHeaderView provideGridHeaderView(GridHeaderViewImpl impl) {
        return impl;
    }

    @Provides
    GridHeaderCellView provideGridColumnHeaderView(GridHeaderCellViewImpl view) {
        return view;
    }
    
    @Provides
    EntityGraphFilterTokenView provideEntityGraphFilterTokenView(EntityGraphFilterTokenViewImpl impl) {
        return impl;
    }

    @Provides
    FormEditorView provideFormEditorView(FormEditorViewImpl impl) {
        return impl;
    }

    @Provides
    MultiChoiceControlDescriptorView provideMultiChoiceControlDescriptorView(MultiChoiceControlDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    ChoiceListSourceDescriptorView provideChoiceListSourceDescriptorView(ChoiceListSourceDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    FixedChoiceListSourceDescriptorView provideFixedChoiceListSourceDescriptorView(FixedChoiceListSourceDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    DynamicChoiceListSourceDescriptorView provideDynamicChoiceListSourceDescriptorView(DynamicChoiceListSourceDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    FormSubjectFactoryDescriptorView provideFormSubjectFactoryDescriptorView(FormSubjectFactoryDescriptorViewImpl impl) {
        return impl;
    }

    @Provides
    EntityFormSelectorView provideEntityFormSelectorView(EntityFormSelectorViewImpl impl) {
        return impl;
    }

    @Provides
    FormIdView provideFormIdView(FormIdViewImpl impl) {
        return impl;
    }

    @Provides
    FormStackView provideFormStackView(FormStackViewImpl impl) {
        return impl;
    }

    @Provides
    RelationshipValueCriteriaView provideRelationshipValueCriteriaView(RelationshipValueCriteriaViewImpl impl) {
        return impl;
    }

    @Provides
    UuidSuffixSettingsView provideUuidSuffixSettingsView(UuidSuffixSettingViewImpl impl) {
        return impl;
    }

    @Provides
    SuppliedNameSuffixSettingsView provideSuppliedNameSuffixSettingsView(SuppliedNameSuffixSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    OboIdSuffixSettingsView provideOboIdSuffixSettingsView(OboIdSuffixSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    EntityCrudKitPrefixSettingsView provideEntityCrudKitPrefixSettingsView(EntityCrudKitPrefixSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    EntityCrudKitSettingsView provideEntityCrudKitSettingsView(EntityCrudKitSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    EntityCrudKitSuffixSettingsView provideEntityCrudKitSuffixSettingsView(EntityCrudKitSuffixSettingsViewImpl impl) {
        return impl;
    }

    @Provides
    ConditionalIriPrefixView provideConditionalIriPrefixesView(ConditionalIriPrefixViewImpl impl) {
        return impl;
    }

    @Provides
    ObjectListPresenter<ConditionalIriPrefix> getConditionalIriPrefixesListPresenter(@Nonnull ObjectListView objectListView,
                                                                                     Provider<ObjectPresenter<ConditionalIriPrefix>> objectListPresenterProvider,
                                                                                     Provider<ObjectListViewHolder> objectViewHolderProvider,
                                                                                     Provider<ConditionalIriPrefix> defaultObjectProvider) {
        return new ObjectListPresenter<>(objectListView,
                                         objectListPresenterProvider,
                                         objectViewHolderProvider,
                                         defaultObjectProvider);
    }

    @Provides
    ObjectPresenter<ConditionalIriPrefix> provideConditionalIriPrefixObjectPresenter(ConditionalIriPrefixPresenter conditionalIriPrefixPresenter) {
        return conditionalIriPrefixPresenter;
    }

    @Provides
    ConditionalIriPrefix provideConditionalIriPrefixProvider() {
        return ConditionalIriPrefix.get();
    }

    @Provides
    CopyFormsFromProjectView provideExportFormToProjectView(CopyFormsFromProjectViewImpl impl) {
        return impl;
    }

    @Provides
    ProjectDetailsView provideProjectDetailsView(ProjectDetailsViewImpl view) {
        return view;
    }

    @Provides
    RepeatabilityView provideRepeatabilityView(RepeatabilityViewImpl impl) {
        return impl;
    }

    @Provides
    EntityFormStackView provideFormStackManagerView(EntityFormStackViewImpl impl) {
        return impl;
    }

    @Provides
    LangTagFilterView provideLangTagFilterView(LangTagFilterViewImpl impl) {
        return impl;
    }

    @Provides
    FormTabBarView provideFormSelectorView(FormTabBarViewImpl impl) {
        return impl;
    }

    @Provides
    FormTabView provideFormSelectorItemView(FormTabViewImpl impl) {
        return impl;
    }

    @Provides
    FormControlContainer provideFormControlContainer(FormControlContainerImpl impl) {
        return impl;
    }

    @Provides
    FormControlStackView provideFormControlStackView(FormControlStackViewImpl impl) {
        return impl;
    }

    @Provides
    FormControlStackRepeatingView provideFormControlStackRepeatingView(FormControlStackRepeatingViewImpl impl) {
        return impl;
    }

    @Provides
    GridRowViewContainer provideGridRowViewContainer(@Nonnull GridRowViewContainerImpl impl) {
        return impl;
    }

    @Provides
    CheckBoxView provideCheckBoxView(CheckBoxViewImpl impl) {
        return impl;
    }

    @Provides
    GridColumnFilterView provideGridColumnFilterView(GridColumnFilterViewImpl impl) {
        return impl;
    }

    @Provides
    TextControlFilterView provideTextControlFilterView(TextControlFilterViewImpl impl) {
        return impl;
    }

    @Provides
    NumberControlFilterView provideNumberControlFilterView(NumberControlFilterViewImpl impl) {
        return impl;
    }

    @Provides
    GridFilterView provideGridFilterView(GridFilterViewImpl impl) {
        return impl;
    }

    @Provides
    GridColumnFilterContainer provideGridColumnFilterContainer(GridColumnFilterContainerImpl impl) {
        return impl;
    }

    @Provides
    EntityNameControlFilterView provideEntityNameControlFilterView(EntityNameControlFilterViewImpl impl) {
        return impl;
    }

    @Provides
    SingleChoiceControlFilterView provideSingleChoiceControlFilterView(SingleChoiceControlFilterViewImpl impl) {
        return impl;
    }

    @Provides
    EntitySearchResultView provideEntitySearchResultView(EntitySearchResultViewImpl impl) {
        return impl;
    }

    @Provides
    SearchResultMatchView provideSearchResultMatchView(SearchResultMatchViewImpl impl) {
        return impl;
    }

    @Provides
    EntitySearchFilterView provideEntitySearchFilterView(EntitySearchFilterViewImpl impl) {
        return impl;
    }

    @Provides
    EntitySearchFilterTokenFieldView provideEntitySearchFilterTokenFieldView(EntitySearchFilterTokenFieldViewImpl impl) {
        return impl;
    }

    @Provides
    FormsManagerObjectView provideFormsManagerObjectView(FormsManagerObjectViewImpl impl) {
        return impl;
    }

    @Provides
    ProjectSettingsHeaderSectionView provideProjectSettingsHeaderSectionView(ProjectSettingsHeaderSectionViewImpl impl) {
        return impl;
    }

    @Provides
    ProjectSettingsImporterView provideProjectSettingsImporterView(ProjectSettingsImporterViewImpl impl) {
        return impl;
    }

    @Provides
    PerspectivesManagerView providePerspectivesManagerView(PerspectivesManagerViewImpl impl) {
        return impl;
    }

    @Provides
    PerspectiveDetailsView providePerspectiveDetailsView(PerspectiveDetailsViewImpl impl) {
        return impl;
    }

    @Provides
    PerspectivesManagerAdminSettingsView providePerspectivesManagerAdminSettingsView(PerspectiveManagerAdminSettingsViewImpl impl) {
        return impl;
    }
}



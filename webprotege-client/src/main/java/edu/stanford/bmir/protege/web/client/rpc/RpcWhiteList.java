package edu.stanford.bmir.protege.web.client.rpc;

import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.bulkop.Operation;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.crud.ConditionalIriPrefix;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.SlackIntegrationSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSetting;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSettings;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchType;
import edu.stanford.bmir.protege.web.shared.search.PrefixNameMatchType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.viz.*;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplPlain;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 *
 * Do not use this class in any client code.  It is here to whitelist objects
 * that don't get added to the serialization whitelist.
 */
public class RpcWhiteList implements Action, Result {


    MultiMatchType multiMatchType;

    HierarchyFilterType hierarchyFilterType;

    ProjectSettings projectSettings;

    SlackIntegrationSettings slackIntegrationSettings;

    WebhookSettings webhookSettings;

    WebhookSetting webhookSetting;

    ProjectWebhookEventType projectWebhookEventType;

    DictionaryLanguageData dictionaryLanguageData;

    DictionaryLanguage dictionaryLanguage;

    AvailableProject availableProject;

    ProjectDetails projectDetails;

    OWLPrimitiveData primitiveData;

    State state;

    ObjectPropertyCharacteristic objectPropertyCharacteristic;

    PropertyValueDescriptor propertyValueDescriptor;

    PropertyValue propertyValue;

    DisplayNameSettings displayNameSettings;

    DictionaryLanguageUsage dictionaryLanguageUsage;

    EntityNameMatchType entityNameMatchType;

    PrefixNameMatchType prefixNameMatchType;

    EntityNameMatchResult entityNameMatchResult;

    InstanceRetrievalMode instanceRetrievalMode;

    ActionExecutionResult actionExecutionResult;

    FormFieldId formFieldId;

    OwlPropertyBinding owlPropertyBinding;

    OwlClassBinding owlClassBinding;

    OwlBinding owlBinding;

    FormControlData formControlData;

    GridColumnId columnId;

    GridControlDescriptor gridFieldDescriptor;

    GridColumnDescriptor gridColumnDescriptor;

    SingleChoiceControlType singleChoiceControlType;

    GridControlData gridControlData;

    GridRowData gridRowData;

    GridCellData gridCellData;

    IRI iri;

    OWLLiteral literal;

    OWLLiteralImplPlain literalImplPlain;

    EntityGraphFilter entityGraphFilter;

    FilterName filterName;

    ProjectUserEntityGraphSettings projectUserEntityGraphSettings;

    EntityGraphSettings entityGraphSettings;

    Operation operation;

    Tag tag;

    EntityGraph entityGraph;

    IsAEdge isAEdge;

    RelationshipEdge relationshipEdge;

    ImmutableSetMultimap immutableSetMultimap;

    FieldRun fieldRun;

    Optionality optionality;

    Repeatability repeatability;

    TextControlDescriptor textFieldDescriptor;

    NumberControlDescriptor numberFieldDescriptor;

    EntityNameControlDescriptor entityNameFieldDescriptor;

    SubFormControlDescriptor subFormFieldDescriptor;

    SingleChoiceControlDescriptor choiceFieldDescriptor;

    ChoiceDescriptor choiceDescriptor;

    ImageControlDescriptor imageFieldDescriptor;

    FormFieldData formFieldData;

    LanguageMap languageMap;

    EdgeCriteria edgeCriteria;

    RelationshipPresence relationshipPresence;

    public HierarchyPositionCriteria getHierarchyPositionCriteria() {
        return hierarchyPositionCriteria;
    }

    public void setHierarchyPositionCriteria(HierarchyPositionCriteria hierarchyPositionCriteria) {
        this.hierarchyPositionCriteria = hierarchyPositionCriteria;
    }

    HierarchyPositionCriteria hierarchyPositionCriteria;

    public ConditionalIriPrefix getConditionalIriPrefix() {
        return conditionalIriPrefix;
    }

    public void setConditionalIriPrefix(ConditionalIriPrefix conditionalIriPrefix) {
        this.conditionalIriPrefix = conditionalIriPrefix;
    }

    ConditionalIriPrefix conditionalIriPrefix;

    public EntityCrudKitSettings getEntityCrudKitSettings() {
        return entityCrudKitSettings;
    }

    public void setEntityCrudKitSettings(EntityCrudKitSettings entityCrudKitSettings) {
        this.entityCrudKitSettings = entityCrudKitSettings;
    }

    EntityCrudKitSettings entityCrudKitSettings;

    public EntityCrudKitSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    public void setSuffixSettings(EntityCrudKitSuffixSettings suffixSettings) {
        this.suffixSettings = suffixSettings;
    }

    public EntityCrudKitPrefixSettings getEntityCrudKitPrefixSettings() {
        return entityCrudKitPrefixSettings;
    }

    public void setEntityCrudKitPrefixSettings(EntityCrudKitPrefixSettings entityCrudKitPrefixSettings) {
        this.entityCrudKitPrefixSettings = entityCrudKitPrefixSettings;
    }

    EntityCrudKitPrefixSettings entityCrudKitPrefixSettings;

    EntityCrudKitSuffixSettings suffixSettings;

    private Color color;

    private Criteria criteria;

    private CompositeRelationshipValueCriteria compositeRelationshipValueCriteria;

    public PlainPropertyValue getPlainPropertyValue() {
        return plainPropertyValue;
    }

    public void setPlainPropertyValue(PlainPropertyValue plainPropertyValue) {
        this.plainPropertyValue = plainPropertyValue;
    }

    PlainPropertyValue plainPropertyValue;

    public UuidFormat getUuidFormat() {
        return uuidFormat;
    }

    public void setUuidFormat(UuidFormat uuidFormat) {
        this.uuidFormat = uuidFormat;
    }

    private UuidFormat uuidFormat;

    public EntityFormSelector getEntityFormSelector() {
        return entityFormSelector;
    }

    public void setEntityFormSelector(EntityFormSelector entityFormSelector) {
        this.entityFormSelector = entityFormSelector;
    }

    public ChoiceListSourceDescriptor getChoiceListSourceDescriptor() {
        return choiceListSourceDescriptor;
    }

    public void setChoiceListSourceDescriptor(ChoiceListSourceDescriptor choiceListSourceDescriptor) {
        this.choiceListSourceDescriptor = choiceListSourceDescriptor;
    }

    EntityFormSelector entityFormSelector;

    ChoiceListSourceDescriptor choiceListSourceDescriptor;

    public FormEntitySubject getFormEntitySubject() {
        return formEntitySubject;
    }

    public void setFormEntitySubject(FormEntitySubject formEntitySubject) {
        this.formEntitySubject = formEntitySubject;
    }

    public void setPrimitiveFormControlData(PrimitiveFormControlData primitiveFormControlData) {
        this.primitiveFormControlData = primitiveFormControlData;
    }

    private FormEntitySubject formEntitySubject;

    public MultiChoiceControlDescriptor getMultiChoiceControlDescriptor() {
        return multiChoiceControlDescriptor;
    }

    public void setMultiChoiceControlDescriptor(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
        this.multiChoiceControlDescriptor = multiChoiceControlDescriptor;
    }

    MultiChoiceControlDescriptor multiChoiceControlDescriptor;

    public RpcWhiteList() {
    }

    public ActionExecutionResult getActionExecutionResult() {
        return actionExecutionResult;
    }

    public void setActionExecutionResult(ActionExecutionResult actionExecutionResult) {
        this.actionExecutionResult = actionExecutionResult;
    }

    public AvailableProject getAvailableProject() {
        return availableProject;
    }

    public void setAvailableProject(AvailableProject availableProject) {
        this.availableProject = availableProject;
    }

    public ChoiceDescriptor getChoiceDescriptor() {
        return choiceDescriptor;
    }

    public void setChoiceDescriptor(ChoiceDescriptor choiceDescriptor) {
        this.choiceDescriptor = choiceDescriptor;
    }

    public SingleChoiceControlDescriptor getChoiceFieldDescriptor() {
        return choiceFieldDescriptor;
    }

    public void setChoiceFieldDescriptor(SingleChoiceControlDescriptor choiceFieldDescriptor) {
        this.choiceFieldDescriptor = choiceFieldDescriptor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public GridColumnId getColumnId() {
        return columnId;
    }

    public void setColumnId(GridColumnId columnId) {
        this.columnId = columnId;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public DictionaryLanguage getDictionaryLanguage() {
        return dictionaryLanguage;
    }

    public void setDictionaryLanguage(DictionaryLanguage dictionaryLanguage) {
        this.dictionaryLanguage = dictionaryLanguage;
    }

    public DictionaryLanguageData getDictionaryLanguageData() {
        return dictionaryLanguageData;
    }

    public void setDictionaryLanguageData(DictionaryLanguageData dictionaryLanguageData) {
        this.dictionaryLanguageData = dictionaryLanguageData;
    }

    public DictionaryLanguageUsage getDictionaryLanguageUsage() {
        return dictionaryLanguageUsage;
    }

    public void setDictionaryLanguageUsage(DictionaryLanguageUsage dictionaryLanguageUsage) {
        this.dictionaryLanguageUsage = dictionaryLanguageUsage;
    }

    public DisplayNameSettings getDisplayNameSettings() {
        return displayNameSettings;
    }

    public void setDisplayNameSettings(DisplayNameSettings displayNameSettings) {
        this.displayNameSettings = displayNameSettings;
    }

    public EdgeCriteria getEdgeCriteria() {
        return edgeCriteria;
    }

    public void setEdgeCriteria(EdgeCriteria edgeCriteria) {
        this.edgeCriteria = edgeCriteria;
    }

    public EntityGraph getEntityGraph() {
        return entityGraph;
    }

    public void setEntityGraph(EntityGraph entityGraph) {
        this.entityGraph = entityGraph;
    }

    public EntityGraphFilter getEntityGraphFilter() {
        return entityGraphFilter;
    }

    public void setEntityGraphFilter(EntityGraphFilter entityGraphFilter) {
        this.entityGraphFilter = entityGraphFilter;
    }

    public EntityGraphSettings getEntityGraphSettings() {
        return entityGraphSettings;
    }

    public void setEntityGraphSettings(EntityGraphSettings entityGraphSettings) {
        this.entityGraphSettings = entityGraphSettings;
    }

    public EntityNameControlDescriptor getEntityNameFieldDescriptor() {
        return entityNameFieldDescriptor;
    }

    public void setEntityNameFieldDescriptor(EntityNameControlDescriptor entityNameFieldDescriptor) {
        this.entityNameFieldDescriptor = entityNameFieldDescriptor;
    }

    public EntityNameMatchResult getEntityNameMatchResult() {
        return entityNameMatchResult;
    }

    public void setEntityNameMatchResult(EntityNameMatchResult entityNameMatchResult) {
        this.entityNameMatchResult = entityNameMatchResult;
    }

    public EntityNameMatchType getEntityNameMatchType() {
        return entityNameMatchType;
    }

    public void setEntityNameMatchType(EntityNameMatchType entityNameMatchType) {
        this.entityNameMatchType = entityNameMatchType;
    }

    public FieldRun getFieldRun() {
        return fieldRun;
    }

    public void setFieldRun(FieldRun fieldRun) {
        this.fieldRun = fieldRun;
    }

    public FilterName getFilterName() {
        return filterName;
    }

    public void setFilterName(FilterName filterName) {
        this.filterName = filterName;
    }

    public FormControlData getFormControlData() {
        return formControlData;
    }

    public void setFormControlData(FormControlData formControlData) {
        this.formControlData = formControlData;
    }

    public FormFieldData getFormFieldData() {
        return formFieldData;
    }

    public void setFormFieldData(FormFieldData formFieldData) {
        this.formFieldData = formFieldData;
    }

    public FormFieldId getFormFieldId() {
        return formFieldId;
    }

    public void setFormFieldId(FormFieldId formFieldId) {
        this.formFieldId = formFieldId;
    }

    public GridCellData getGridCellData() {
        return gridCellData;
    }

    public void setGridCellData(GridCellData gridCellData) {
        this.gridCellData = gridCellData;
    }

    public GridColumnDescriptor getGridColumnDescriptor() {
        return gridColumnDescriptor;
    }

    public void setGridColumnDescriptor(GridColumnDescriptor gridColumnDescriptor) {
        this.gridColumnDescriptor = gridColumnDescriptor;
    }

    public GridControlData getGridControlData() {
        return gridControlData;
    }

    public void setGridControlData(GridControlData gridControlData) {
        this.gridControlData = gridControlData;
    }

    public GridControlDescriptor getGridFieldDescriptor() {
        return gridFieldDescriptor;
    }

    public void setGridFieldDescriptor(GridControlDescriptor gridFieldDescriptor) {
        this.gridFieldDescriptor = gridFieldDescriptor;
    }

    public GridRowData getGridRowData() {
        return gridRowData;
    }

    public void setGridRowData(GridRowData gridRowData) {
        this.gridRowData = gridRowData;
    }

    public HierarchyFilterType getHierarchyFilterType() {
        return hierarchyFilterType;
    }

    public void setHierarchyFilterType(HierarchyFilterType hierarchyFilterType) {
        this.hierarchyFilterType = hierarchyFilterType;
    }

    public ImageControlDescriptor getImageFieldDescriptor() {
        return imageFieldDescriptor;
    }

    public void setImageFieldDescriptor(ImageControlDescriptor imageFieldDescriptor) {
        this.imageFieldDescriptor = imageFieldDescriptor;
    }

    public ImmutableSetMultimap getImmutableSetMultimap() {
        return immutableSetMultimap;
    }

    public void setImmutableSetMultimap(ImmutableSetMultimap immutableSetMultimap) {
        this.immutableSetMultimap = immutableSetMultimap;
    }

    public InstanceRetrievalMode getInstanceRetrievalMode() {
        return instanceRetrievalMode;
    }

    public void setInstanceRetrievalMode(InstanceRetrievalMode instanceRetrievalMode) {
        this.instanceRetrievalMode = instanceRetrievalMode;
    }

    public IRI getIri() {
        return iri;
    }

    public void setIri(IRI iri) {
        this.iri = iri;
    }

    public IsAEdge getIsAEdge() {
        return isAEdge;
    }

    public void setIsAEdge(IsAEdge isAEdge) {
        this.isAEdge = isAEdge;
    }

    public LanguageMap getLanguageMap() {
        return languageMap;
    }

    public void setLanguageMap(LanguageMap languageMap) {
        this.languageMap = languageMap;
    }

    public OWLLiteral getLiteral() {
        return literal;
    }

    public void setLiteral(OWLLiteral literal) {
        this.literal = literal;
    }

    public OWLLiteralImplPlain getLiteralImplPlain() {
        return literalImplPlain;
    }

    public void setLiteralImplPlain(OWLLiteralImplPlain literalImplPlain) {
        this.literalImplPlain = literalImplPlain;
    }

    public MultiMatchType getMultiMatchType() {
        return multiMatchType;
    }

    public void setMultiMatchType(MultiMatchType multiMatchType) {
        this.multiMatchType = multiMatchType;
    }

    public NumberControlDescriptor getNumberFieldDescriptor() {
        return numberFieldDescriptor;
    }

    public void setNumberFieldDescriptor(NumberControlDescriptor numberFieldDescriptor) {
        this.numberFieldDescriptor = numberFieldDescriptor;
    }

    public ObjectPropertyCharacteristic getObjectPropertyCharacteristic() {
        return objectPropertyCharacteristic;
    }

    public void setObjectPropertyCharacteristic(ObjectPropertyCharacteristic objectPropertyCharacteristic) {
        this.objectPropertyCharacteristic = objectPropertyCharacteristic;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Optionality getOptionality() {
        return optionality;
    }

    public void setOptionality(Optionality optionality) {
        this.optionality = optionality;
    }

    public OwlBinding getOwlBinding() {
        return owlBinding;
    }

    public void setOwlBinding(OwlBinding owlBinding) {
        this.owlBinding = owlBinding;
    }

    public OwlClassBinding getOwlClassBinding() {
        return owlClassBinding;
    }

    public void setOwlClassBinding(OwlClassBinding owlClassBinding) {
        this.owlClassBinding = owlClassBinding;
    }

    public OwlPropertyBinding getOwlPropertyBinding() {
        return owlPropertyBinding;
    }

    public void setOwlPropertyBinding(OwlPropertyBinding owlPropertyBinding) {
        this.owlPropertyBinding = owlPropertyBinding;
    }

    public PrefixNameMatchType getPrefixNameMatchType() {
        return prefixNameMatchType;
    }

    public void setPrefixNameMatchType(PrefixNameMatchType prefixNameMatchType) {
        this.prefixNameMatchType = prefixNameMatchType;
    }

    public OWLPrimitiveData getPrimitiveData() {
        return primitiveData;
    }

    public void setPrimitiveData(OWLPrimitiveData primitiveData) {
        this.primitiveData = primitiveData;
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    public void setProjectSettings(ProjectSettings projectSettings) {
        this.projectSettings = projectSettings;
    }

    public ProjectUserEntityGraphSettings getProjectUserEntityGraphSettings() {
        return projectUserEntityGraphSettings;
    }

    public void setProjectUserEntityGraphSettings(ProjectUserEntityGraphSettings projectUserEntityGraphSettings) {
        this.projectUserEntityGraphSettings = projectUserEntityGraphSettings;
    }

    public ProjectWebhookEventType getProjectWebhookEventType() {
        return projectWebhookEventType;
    }

    public void setProjectWebhookEventType(ProjectWebhookEventType projectWebhookEventType) {
        this.projectWebhookEventType = projectWebhookEventType;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
    }

    public PropertyValueDescriptor getPropertyValueDescriptor() {
        return propertyValueDescriptor;
    }

    public void setPropertyValueDescriptor(PropertyValueDescriptor propertyValueDescriptor) {
        this.propertyValueDescriptor = propertyValueDescriptor;
    }

    public RelationshipEdge getRelationshipEdge() {
        return relationshipEdge;
    }

    public void setRelationshipEdge(RelationshipEdge relationshipEdge) {
        this.relationshipEdge = relationshipEdge;
    }

    public RelationshipPresence getRelationshipPresence() {
        return relationshipPresence;
    }

    public void setRelationshipPresence(RelationshipPresence relationshipPresence) {
        this.relationshipPresence = relationshipPresence;
    }

    public PrimitiveFormControlData getPrimitiveFormControlData() {
        return primitiveFormControlData;
    }

    private PrimitiveFormControlData primitiveFormControlData;

    public Repeatability getRepeatability() {
        return repeatability;
    }

    public void setRepeatability(Repeatability repeatability) {
        this.repeatability = repeatability;
    }

    public SingleChoiceControlType getSingleChoiceControlType() {
        return singleChoiceControlType;
    }

    public void setSingleChoiceControlType(SingleChoiceControlType singleChoiceControlType) {
        this.singleChoiceControlType = singleChoiceControlType;
    }

    public SlackIntegrationSettings getSlackIntegrationSettings() {
        return slackIntegrationSettings;
    }

    public void setSlackIntegrationSettings(SlackIntegrationSettings slackIntegrationSettings) {
        this.slackIntegrationSettings = slackIntegrationSettings;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public SubFormControlDescriptor getSubFormFieldDescriptor() {
        return subFormFieldDescriptor;
    }

    public void setSubFormFieldDescriptor(SubFormControlDescriptor subFormFieldDescriptor) {
        this.subFormFieldDescriptor = subFormFieldDescriptor;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public TextControlDescriptor getTextFieldDescriptor() {
        return textFieldDescriptor;
    }

    public void setTextFieldDescriptor(TextControlDescriptor textFieldDescriptor) {
        this.textFieldDescriptor = textFieldDescriptor;
    }

    public WebhookSetting getWebhookSetting() {
        return webhookSetting;
    }

    public void setWebhookSetting(WebhookSetting webhookSetting) {
        this.webhookSetting = webhookSetting;
    }

    public WebhookSettings getWebhookSettings() {
        return webhookSettings;
    }

    public void setWebhookSettings(WebhookSettings webhookSettings) {
        this.webhookSettings = webhookSettings;
    }


    public CompositeRelationshipValueCriteria getCompositeRelationshipValueCriteria() {
        return compositeRelationshipValueCriteria;
    }

    public void setCompositeRelationshipValueCriteria(CompositeRelationshipValueCriteria compositeRelationshipValueCriteria) {
        this.compositeRelationshipValueCriteria = compositeRelationshipValueCriteria;
    }


}

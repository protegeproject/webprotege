package edu.stanford.bmir.protege.web.client.rpc;

import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.bulkop.Operation;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2018
 *
 * Do not use this class in any client code.  It is here to whitelist objects
 * that don't get added to the serialization whitelist.
 */
public class RpcWhiteList implements Action, Result {


    private Color color;

    private Criteria criteria;

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

    public GridColumnId getColumnId() {
        return columnId;
    }

    public void setColumnId(GridColumnId columnId) {
        this.columnId = columnId;
    }

    GridColumnId columnId;

    GridControlDescriptor gridFieldDescriptor;

    GridColumnDescriptor gridColumnDescriptor;

    public GridControlDescriptor getGridFieldDescriptor() {
        return gridFieldDescriptor;
    }

    public void setGridFieldDescriptor(GridControlDescriptor gridFieldDescriptor) {
        this.gridFieldDescriptor = gridFieldDescriptor;
    }

    public GridColumnDescriptor getGridColumnDescriptor() {
        return gridColumnDescriptor;
    }

    public void setGridColumnDescriptor(GridColumnDescriptor gridColumnDescriptor) {
        this.gridColumnDescriptor = gridColumnDescriptor;
    }

    EntityGraphFilter entityGraphFilter;

    public FilterName getFilterName() {
        return filterName;
    }

    public void setFilterName(FilterName filterName) {
        this.filterName = filterName;
    }

    FilterName filterName;

    public EntityGraphFilter getEntityGraphFilter() {
        return entityGraphFilter;
    }

    public void setEntityGraphFilter(EntityGraphFilter entityGraphFilter) {
        this.entityGraphFilter = entityGraphFilter;
    }

    public ProjectUserEntityGraphSettings getProjectUserEntityGraphSettings() {
        return projectUserEntityGraphSettings;
    }

    public void setProjectUserEntityGraphSettings(ProjectUserEntityGraphSettings projectUserEntityGraphSettings) {
        this.projectUserEntityGraphSettings = projectUserEntityGraphSettings;
    }



    ProjectUserEntityGraphSettings projectUserEntityGraphSettings;

    public EntityGraphSettings getEntityGraphSettings() {
        return entityGraphSettings;
    }

    public void setEntityGraphSettings(EntityGraphSettings entityGraphSettings) {
        this.entityGraphSettings = entityGraphSettings;
    }

    EntityGraphSettings entityGraphSettings;

    public ActionExecutionResult getActionExecutionResult() {
        return actionExecutionResult;
    }

    public void setActionExecutionResult(ActionExecutionResult actionExecutionResult) {
        this.actionExecutionResult = actionExecutionResult;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    Operation operation;

    Tag tag;

    public EntityGraph getEntityGraph() {
        return entityGraph;
    }

    public void setEntityGraph(EntityGraph entityGraph) {
        this.entityGraph = entityGraph;
    }

    public IsAEdge getIsAEdge() {
        return isAEdge;
    }

    public void setIsAEdge(IsAEdge isAEdge) {
        this.isAEdge = isAEdge;
    }

    public RelationshipEdge getRelationshipEdge() {
        return relationshipEdge;
    }

    public void setRelationshipEdge(RelationshipEdge relationshipEdge) {
        this.relationshipEdge = relationshipEdge;
    }

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

    ChoiceControlDescriptor choiceFieldDescriptor;

    ChoiceDescriptor choiceDescriptor;

    ImageControlDescriptor imageFieldDescriptor;



    public LanguageMap getLanguageMap() {
        return languageMap;
    }

    public void setLanguageMap(LanguageMap languageMap) {
        this.languageMap = languageMap;
    }

    LanguageMap languageMap;

    public EdgeCriteria getEdgeCriteria() {
        return edgeCriteria;
    }

    public void setEdgeCriteria(EdgeCriteria edgeCriteria) {
        this.edgeCriteria = edgeCriteria;
    }

    EdgeCriteria edgeCriteria;

    public RelationshipPresence getRelationshipPresence() {
        return relationshipPresence;
    }

    public void setRelationshipPresence(RelationshipPresence relationshipPresence) {
        this.relationshipPresence = relationshipPresence;
    }

    RelationshipPresence relationshipPresence;


    public RpcWhiteList() {
    }

    public ImmutableSetMultimap getImmutableSetMultimap() {
        return immutableSetMultimap;
    }

    public void setImmutableSetMultimap(ImmutableSetMultimap immutableSetMultimap) {
        this.immutableSetMultimap = immutableSetMultimap;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public MultiMatchType getMultiMatchType() {
        return multiMatchType;
    }

    public void setMultiMatchType(MultiMatchType multiMatchType) {
        this.multiMatchType = multiMatchType;
    }

    public HierarchyFilterType getHierarchyFilterType() {
        return hierarchyFilterType;
    }

    public void setHierarchyFilterType(HierarchyFilterType hierarchyFilterType) {
        this.hierarchyFilterType = hierarchyFilterType;
    }

    public WebhookSettings getWebhookSettings() {
        return webhookSettings;
    }

    public void setWebhookSettings(WebhookSettings webhookSettings) {
        this.webhookSettings = webhookSettings;
    }

    public WebhookSetting getWebhookSetting() {
        return webhookSetting;
    }

    public void setWebhookSetting(WebhookSetting webhookSetting) {
        this.webhookSetting = webhookSetting;
    }

    public ProjectWebhookEventType getProjectWebhookEventType() {
        return projectWebhookEventType;
    }

    public void setProjectWebhookEventType(ProjectWebhookEventType projectWebhookEventType) {
        this.projectWebhookEventType = projectWebhookEventType;
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    public void setProjectSettings(ProjectSettings projectSettings) {
        this.projectSettings = projectSettings;
    }

    public SlackIntegrationSettings getSlackIntegrationSettings() {
        return slackIntegrationSettings;
    }

    public void setSlackIntegrationSettings(SlackIntegrationSettings slackIntegrationSettings) {
        this.slackIntegrationSettings = slackIntegrationSettings;
    }

    public DictionaryLanguageData getDictionaryLanguageData() {
        return dictionaryLanguageData;
    }

    public void setDictionaryLanguageData(DictionaryLanguageData dictionaryLanguageData) {
        this.dictionaryLanguageData = dictionaryLanguageData;
    }

    public DictionaryLanguage getDictionaryLanguage() {
        return dictionaryLanguage;
    }

    public void setDictionaryLanguage(DictionaryLanguage dictionaryLanguage) {
        this.dictionaryLanguage = dictionaryLanguage;
    }

    public AvailableProject getAvailableProject() {
        return availableProject;
    }

    public void setAvailableProject(AvailableProject availableProject) {
        this.availableProject = availableProject;
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public OWLPrimitiveData getPrimitiveData() {
        return primitiveData;
    }

    public void setPrimitiveData(OWLPrimitiveData primitiveData) {
        this.primitiveData = primitiveData;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ObjectPropertyCharacteristic getObjectPropertyCharacteristic() {
        return objectPropertyCharacteristic;
    }

    public void setObjectPropertyCharacteristic(ObjectPropertyCharacteristic objectPropertyCharacteristic) {
        this.objectPropertyCharacteristic = objectPropertyCharacteristic;
    }

    public PropertyValueDescriptor getPropertyValueDescriptor() {
        return propertyValueDescriptor;
    }

    public void setPropertyValueDescriptor(PropertyValueDescriptor propertyValueDescriptor) {
        this.propertyValueDescriptor = propertyValueDescriptor;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
    }

    public DisplayNameSettings getDisplayNameSettings() {
        return displayNameSettings;
    }

    public void setDisplayNameSettings(DisplayNameSettings displayNameSettings) {
        this.displayNameSettings = displayNameSettings;
    }

    public DictionaryLanguageUsage getDictionaryLanguageUsage() {
        return dictionaryLanguageUsage;
    }

    public void setDictionaryLanguageUsage(DictionaryLanguageUsage dictionaryLanguageUsage) {
        this.dictionaryLanguageUsage = dictionaryLanguageUsage;
    }

    public EntityNameMatchType getEntityNameMatchType() {
        return entityNameMatchType;
    }

    public PrefixNameMatchType getPrefixNameMatchType() {
        return prefixNameMatchType;
    }

    public void setEntityNameMatchType(EntityNameMatchType entityNameMatchType) {
        this.entityNameMatchType = entityNameMatchType;
    }

    public void setPrefixNameMatchType(PrefixNameMatchType prefixNameMatchType) {
        this.prefixNameMatchType = prefixNameMatchType;
    }

    public EntityNameMatchResult getEntityNameMatchResult() {
        return entityNameMatchResult;
    }

    public void setEntityNameMatchResult(EntityNameMatchResult entityNameMatchResult) {
        this.entityNameMatchResult = entityNameMatchResult;
    }

    public InstanceRetrievalMode getInstanceRetrievalMode() {
        return instanceRetrievalMode;
    }

    public void setInstanceRetrievalMode(InstanceRetrievalMode instanceRetrievalMode) {
        this.instanceRetrievalMode = instanceRetrievalMode;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public FormFieldId getFormFieldId() {
        return formFieldId;
    }

    public void setFormFieldId(FormFieldId formFieldId) {
        this.formFieldId = formFieldId;
    }

    public FieldRun getFieldRun() {
        return fieldRun;
    }

    public void setFieldRun(FieldRun fieldRun) {
        this.fieldRun = fieldRun;
    }

    public Optionality getOptionality() {
        return optionality;
    }

    public void setOptionality(Optionality optionality) {
        this.optionality = optionality;
    }

    public Repeatability getRepeatability() {
        return repeatability;
    }

    public void setRepeatability(Repeatability repeatability) {
        this.repeatability = repeatability;
    }

    public TextControlDescriptor getTextFieldDescriptor() {
        return textFieldDescriptor;
    }

    public NumberControlDescriptor getNumberFieldDescriptor() {
        return numberFieldDescriptor;
    }

    public EntityNameControlDescriptor getEntityNameFieldDescriptor() {
        return entityNameFieldDescriptor;
    }

    public SubFormControlDescriptor getSubFormFieldDescriptor() {
        return subFormFieldDescriptor;
    }

    public ChoiceControlDescriptor getChoiceFieldDescriptor() {
        return choiceFieldDescriptor;
    }

    public ChoiceDescriptor getChoiceDescriptor() {
        return choiceDescriptor;
    }

    public ImageControlDescriptor getImageFieldDescriptor() {
        return imageFieldDescriptor;
    }

    public OwlPropertyBinding getOwlPropertyBinding() {
        return owlPropertyBinding;
    }

    public void setOwlPropertyBinding(OwlPropertyBinding owlPropertyBinding) {
        this.owlPropertyBinding = owlPropertyBinding;
    }

    public OwlClassBinding getOwlClassBinding() {
        return owlClassBinding;
    }

    public void setOwlClassBinding(OwlClassBinding owlClassBinding) {
        this.owlClassBinding = owlClassBinding;
    }

    public OwlBinding getOwlBinding() {
        return owlBinding;
    }

    public void setOwlBinding(OwlBinding owlBinding) {
        this.owlBinding = owlBinding;
    }

    public void setTextFieldDescriptor(TextControlDescriptor textFieldDescriptor) {
        this.textFieldDescriptor = textFieldDescriptor;
    }

    public void setNumberFieldDescriptor(NumberControlDescriptor numberFieldDescriptor) {
        this.numberFieldDescriptor = numberFieldDescriptor;
    }

    public void setEntityNameFieldDescriptor(EntityNameControlDescriptor entityNameFieldDescriptor) {
        this.entityNameFieldDescriptor = entityNameFieldDescriptor;
    }

    public void setSubFormFieldDescriptor(SubFormControlDescriptor subFormFieldDescriptor) {
        this.subFormFieldDescriptor = subFormFieldDescriptor;
    }

    public void setChoiceFieldDescriptor(ChoiceControlDescriptor choiceFieldDescriptor) {
        this.choiceFieldDescriptor = choiceFieldDescriptor;
    }

    public void setChoiceDescriptor(ChoiceDescriptor choiceDescriptor) {
        this.choiceDescriptor = choiceDescriptor;
    }

    public void setImageFieldDescriptor(ImageControlDescriptor imageFieldDescriptor) {
        this.imageFieldDescriptor = imageFieldDescriptor;
    }


}

package edu.stanford.bmir.protege.web.client;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
@DefaultLocale
public interface FormsMessages extends com.google.gwt.i18n.client.Messages {

    @DefaultMessage("Expand/collapse element")
    @Key("expandCollapse")
    String expandCollapse();

    @DefaultMessage("Forms")
    @Key("forms.title")
    String forms_Title();

    @DefaultMessage("No form selected")
    @Key("noFormSelected")
    String noFormSelected();

    @DefaultMessage("Available forms")
    @Key("availableForms")
    String availableForms();

    @DefaultMessage("Add form")
    @Key("addForm")
    String addForm();

    @DefaultMessage("Add form element")
    @Key("addFormElement")
    String addFormElement();


    @DefaultMessage("Enter help text")
    @Key("enterHelpText")
    String enterHelpText();

    @DefaultMessage("Enter label")
    @Key("enterLabel")
    String enterLabel();

    @DefaultMessage("Enter placeholder")
    @Key("enterPlaceholder")
    String enterPlaceholder();

    @DefaultMessage("Project Forms")
    @Key("projectForms.title")
    String projectForms_Title();

    @DefaultMessage("Delete form element")
    @Key("removeFormElement")
    String removeFormElement();

    @DefaultMessage("Move form element down")
    @Key("moveFormElementDown")
    String moveFormElementDown();


    @DefaultMessage("Move form element up")
    @Key("moveFormElementUp")
    String moveFormElementUp();

    @DefaultMessage("Delete <b>{0}</b>?")
    @Key("deleteFormElementConfirmation.title")
    String deleteFormElementConfirmation_Title(String formElementId);

    @DefaultMessage("Are you sure that you want to delete the <b>{0}</b> form element?  This cannot be undone.")
    @Key("deleteFormElementConfirmation.message")
    String deleteFormElementConfirmation_Message(String formElementId);

    @DefaultMessage("Subject annotations")
    @Key("subForm.subject.annotations")
    String subFormSubjectAnnotations();

    @DefaultMessage("Subject parents/types")
    @Key("subForm.subject.parents")
    String subFormSubjectParents();

    @DefaultMessage("Subject generated name pattern")
    @Key("subForm.subject.pattern")
    String subFormSubjectGeneratedNamePattern();

    @DefaultMessage("Subject type")
    @Key("subForm.subject.type")
    String subFormSubjectType();
}

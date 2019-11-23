package edu.stanford.bmir.protege.web.client;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
@DefaultLocale
public interface FormsMessages extends com.google.gwt.i18n.client.Messages {

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

    @DefaultMessage("Delete form element")
    @Key("deleteFormElement")
    String deleteFormElement();

    @DefaultMessage("Enter help text")
    @Key("enterHelpText")
    String enterHelpText();

    @DefaultMessage("Enter label")
    @Key("enterLabel")
    String enterLabel();

    @DefaultMessage("Enter placeholder")
    @Key("enterPlaceholder")
    String enterPlaceholder();
}

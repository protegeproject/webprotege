package edu.stanford.bmir.protege.web.client;


import com.google.gwt.safehtml.shared.SafeHtml;

import static com.google.gwt.i18n.client.LocalizableResource.*;

@DefaultLocale()
public interface Messages extends com.google.gwt.i18n.client.Messages {

    
    @DefaultMessage("About")
    @Key("about")
    String about();

    @DefaultMessage("Back")
    @Key("back")
    String back();
    
    @DefaultMessage("tab")
    @Key("perspective.tab")
    String perspective_tab();

    @DefaultMessage("Tabs")
    @Key("perspective.tabs")
    String perspective_tabs();

    @DefaultMessage("Add tab")
    @Key("perspective.perspective_addTab")
    String perspective_addTab();

    
    @DefaultMessage("authored")
    @Key("change.authored")
    String change_authored();

    
    @DefaultMessage("changes")
    @Key("change.changes")
    String change_changes();

    
    @DefaultMessage("Changes on")
    @Key("change.changesOn")
    String change_changesOn();

    
    @DefaultMessage("Download revision")
    @Key("change.downloadRevision")
    String change_downloadRevision();

    
    @DefaultMessage("more changes not shown here")
    @Key("change.moreChangesNotShownHere")
    String change_moreChangesNotShownHere();

    
    @DefaultMessage("You do not have permission to view changes in this")
    @Key("change.permissionDenied")
    String change_permissionDenied();

    
    @DefaultMessage("Plus")
    @Key("change.plus")
    String change_plus();

    
    @DefaultMessage("Revert")
    @Key("change.revert")
    String change_revert();

    
    @DefaultMessage("Revert changes in revision")
    @Key("change.revertChangesInRevision")
    String change_revertChangesInRevision();

    
    @DefaultMessage("Do you want to revert the changes in this revision?")
    @Key("change.revertChangesInRevisionQuestion")
    String change_revertChangesInRevisionQuestion();

    
    @DefaultMessage("Revert Changes?")
    @Key("change.revertChangesQuestion")
    String change_revertChangesQuestion();

    @DefaultMessage("Changes in revision {0} have been reverted")
    @Key("change.revertSuccess")
    String change_revertChangesInRevisionSuccessful(long value);

    @DefaultMessage("Change Email Address")
    @Key("changeEmailAddress")
    String changeEmailAddress();

    
    @DefaultMessage("Change Password")
    @Key("changePassword")
    String changePassword();

    
    @DefaultMessage("Changes by Entity")
    @Key("changesByEntity")
    String changesByEntity();

    
    @DefaultMessage("Class IRI")
    @Key("classIri")
    String classIri();

    
    @DefaultMessage("Classes")
    @Key("classes")
    String classes();

    
    @DefaultMessage("Comments")
    @Key("comments")
    String comments();

    
    @DefaultMessage("by")
    @Key("comments.by")
    String comments_by();

    
    @DefaultMessage("Comment")
    @Key("comments.comment")
    String comments_comment();

    
    @DefaultMessage("Commented entities")
    @Key("comments.commentedEntities")
    String comments_commentedEntities();

    
    @DefaultMessage("Last comment")
    @Key("comments.lastComment")
    String comments_lastComment();

    
    @DefaultMessage("resolved")
    @Key("comments.resolved")
    String comments_resolved();

    
    @DefaultMessage("unresolved")
    @Key("comments.unresolved")
    String comments_unresolved();

    
    @DefaultMessage("Create")
    @Key("create")
    String create();

    
    @DefaultMessage("Enter one name per line (press CTRL+ENTER to accept and close panel)")
    @Key("createEntityInstructions")
    String createEntityInstructions();

    
    @DefaultMessage("Create New Project")
    @Key("createProject")
    String createProject();

    @DefaultMessage("Add")
    @Key("add")
    String add();
    
    @DefaultMessage("Delete")
    @Key("delete")
    String delete();

    
    @DefaultMessage("Are you sure you want to delete the {0} <strong>{1}</strong>?")
    @Key("delete.entity.msg")
    String delete_entity_msg(String arg0,  String arg1);

    
    @DefaultMessage("Delete {0}")
    @Key("delete.entity.title")
    String delete_entity_title(String broswerText);

    
    @DefaultMessage("Delete")
    @Key("deleteComment")
    String deleteComment();

    
    @DefaultMessage("Are you sure that you want to delete this comment?  This cannot be undone.")
    @Key("deleteCommentConfirmationBoxText")
    String deleteCommentConfirmationBoxText();

    
    @DefaultMessage("Delete Comment?")
    @Key("deleteCommentConfirmationBoxTitle")
    String deleteCommentConfirmationBoxTitle();

    
    @DefaultMessage("Apply")
    @Key("dialog.apply")
    String dialog_apply();

    
    @DefaultMessage("Cancel")
    @Key("dialog.cancel")
    String dialog_cancel();

    
    @DefaultMessage("Close")
    @Key("dialog.close")
    String dialog_close();

    
    @DefaultMessage("No")
    @Key("dialog.no")
    String dialog_no();

    
    @DefaultMessage("OK")
    @Key("dialog.ok")
    String dialog_ok();

    
    @DefaultMessage("Select")
    @Key("dialog.select")
    String dialog_select();

    
    @DefaultMessage("Yes")
    @Key("dialog.yes")
    String dialog_yes();

    
    @DefaultMessage("Direct Link")
    @Key("directLink")
    String directLink();

    
    @DefaultMessage("Display resolved threads")
    @Key("discussionThread.DisplayResolvedThreads")
    String discussionThread_DisplayResolvedThreads();

    
    @DefaultMessage("Re-open")
    @Key("discussionThread.Reopen")
    String discussionThread_Reopen();

    
    @DefaultMessage("Resolve")
    @Key("discussionThread.Resolve")
    String discussionThread_Resolve();

    
    @DefaultMessage("You do not have permission to view comments in this project")
    @Key("discussionThread.ViewingForbidden")
    String discussionThread_ViewingForbidden();

    
    @DefaultMessage("Download")
    @Key("download")
    String download();

    
    @DefaultMessage("Edit")
    @Key("editComment")
    String editComment();

    
    @DefaultMessage("Enter search string to filter list")
    @Key("enterSearchStringToFilterList")
    String enterSearchStringToFilterList();

    
    @DefaultMessage("Error")
    @Key("error")
    String error();

    
    @DefaultMessage("An error has occurred.  Please refresh your browser and try again.")
    @Key("error.anErrorHasOccurred")
    String error_anErrorHasOccurred();

    
    @DefaultMessage("WebProtégé cannot connect to the server.  Please check your network connection.")
    @Key("error.connectionError.msg")
    String error_connectionError_msg();

    
    @DefaultMessage("Connection Error")
    @Key("error.connectionError.title")
    String error_connectionError_title();

    
    @DefaultMessage("An error has occurred.  Please refresh your browser and try again.")
    @Key("error.general")
    String error_general();

    
    @DefaultMessage("You do not have permission to carry out the specified action.")
    @Key("error.permissionError.msg")
    String error_permissionError_msg();

    
    @DefaultMessage("Permission Denied")
    @Key("error.permissionError.title")
    String error_permissionError_title();

    
    @DefaultMessage("Please refresh your browser")
    @Key("error.refreshBrowser")
    String error_refreshBrowser();

    
    @DefaultMessage("Server Error")
    @Key("error.serverError")
    String error_serverError();

    
    @DefaultMessage("Status Code")
    @Key("error.statusCode")
    String error_statusCode();

    
    @DefaultMessage("WebProtégé has been updated.  Please empty your browser caches and reload your browser.")
    @Key("error.upgraded")
    String error_upgraded();

    
    @DefaultMessage("WebProtégé has encountered an error.  Please try again.")
    @Key("error.webProtegeHasEncounteredAnErrorPleaseTryAgain")
    String error_webProtegeHasEncounteredAnErrorPleaseTryAgain();

    
    @DefaultMessage("Forbidden")
    @Key("forbidden")
    String forbidden();

    
    @DefaultMessage("Forgot username or password")
    @Key("forgotUserNameOrPassword")
    String forgotUserNameOrPassword();

    @DefaultMessage("Forms")
    @Key("editProjectForms")
    String forms_EditProjectForms();


    @DefaultMessage("Annotations")
    @Key("frame.annotations")
    String frame_annotations();

    
    @DefaultMessage("Parents")
    @Key("frame.classes")
    String frame_classes();

    
    @DefaultMessage("Domain")
    @Key("frame.domain")
    String frame_domain();

    @DefaultMessage("Characteristics")
    @Key("frame.characteristics")
    String frame_characteristics();

    
    @DefaultMessage("Enter a class name")
    @Key("frame.enterAClassName")
    String frame_enterAClassName();

    
    @DefaultMessage("Enter a datatype name")
    @Key("frame.enterADatatype")
    String frame_enterADatatype();

    
    @DefaultMessage("Enter an individual name")
    @Key("frame.enterIndividualName")
    String frame_enterIndividualName();

    
    @DefaultMessage("Enter property")
    @Key("frame.enterProperty")
    String frame_enterProperty();

    
    @DefaultMessage("Enter value")
    @Key("frame.enterValue")
    String frame_enterValue();

    
    @DefaultMessage("Functional")
    @Key("frame.functional")
    String frame_functional();

    
    @DefaultMessage("lang")
    @Key("frame.lang")
    String frame_lang();

    
    @DefaultMessage("Range")
    @Key("frame.range")
    String frame_range();

    
    @DefaultMessage("Relationships")
    @Key("frame.relationships")
    String frame_relationships();

    
    @DefaultMessage("Same As")
    @Key("frame.sameIndividual")
    String frame_sameIndividual();

    
    @DefaultMessage("Single valued")
    @Key("frame.singleValued")
    String frame_singleValued();

    
    @DefaultMessage("Transitive")
    @Key("frame.transitive")
    String frame_transitive();

    
    @DefaultMessage("Types")
    @Key("frame.types")
    String frame_types();

    
    @DefaultMessage("Help")
    @Key("help")
    String help();

    
    @DefaultMessage("Annotation Properties")
    @Key("hierarchy.annotationproperties")
    String hierarchy_annotationproperties();

    
    @DefaultMessage("Data Properties")
    @Key("hierarchy.dataproperties")
    String hierarchy_dataproperties();

    
    @DefaultMessage("Object Properties")
    @Key("hierarchy.objectproperties")
    String hierarchy_objectproperties();

    
    @DefaultMessage("History")
    @Key("history")
    String history();

    
    @DefaultMessage("Home")
    @Key("home")
    String home();

    
    @DefaultMessage("Individuals")
    @Key("individuals")
    String individuals();

    
    @DefaultMessage("instance")
    @Key("instance")
    String instance();

    
    @DefaultMessage("IRI")
    @Key("iri")
    String iri();

    
    @DefaultMessage("Last modified")
    @Key("lastModified")
    String lastModified();

    
    @DefaultMessage("Last opened")
    @Key("lastOpened")
    String lastOpened();

    
    @DefaultMessage("Last updated")
    @Key("lastUpdated")
    String lastUpdated();

    
    @DefaultMessage("Please enter your password.")
    @Key("login.enterPassword")
    String login_enterPassword();

    
    @DefaultMessage("Please enter your user name.")
    @Key("login.enterUserName")
    String login_enterUserName();

    
    @DefaultMessage("Unable to log in.  User name or password is incorrect.")
    @Key("login.error")
    String login_error();

    
    @DefaultMessage("Markdown supported")
    @Key("markdownSupported")
    String markdownSupported();

    
    @DefaultMessage("Move to Trash")
    @Key("moveToTrash")
    String moveToTrash();

    
    @DefaultMessage("New Entity Settings")
    @Key("newEntitySettings")
    String newEntitySettings();

    
    @DefaultMessage("Digit count")
    @Key("newEntitySettings.digitCount")
    String newEntitySettings_digitCount();

    
    @DefaultMessage("IRI Prefix")
    @Key("newEntitySettings.iriPrefix")
    String newEntitySettings_iriPrefix();

    
    @DefaultMessage("IRI Suffix")
    @Key("newEntitySettings.iriSuffix")
    String newEntitySettings_iriSuffix();

    
    @DefaultMessage("Auto-generated OBO Style Id")
    @Key("newEntitySettings.oboId")
    String newEntitySettings_oboId();

    
    @DefaultMessage("Spaces")
    @Key("newEntitySettings.spaces")
    String newEntitySettings_spaces();

    
    @DefaultMessage("Collapse and transform to camel case")
    @Key("newEntitySettings.spaces.collapseAndTransformToCamelCase")
    String newEntitySettings_spaces_collapseAndTransformToCamelCase();

    
    @DefaultMessage("Replace with dashes")
    @Key("newEntitySettings.spaces.replaceWithDashes")
    String newEntitySettings_spaces_replaceWithDashes();

    
    @DefaultMessage("Replace with underscores")
    @Key("newEntitySettings.spaces.replaceWithUnderscores")
    String newEntitySettings_spaces_replaceWithUnderscores();

    
    @DefaultMessage("Supplied name")
    @Key("newEntitySettings.suppliedName")
    String newEntitySettings_suppliedName();

    
    @DefaultMessage("User specific ranges")
    @Key("newEntitySettings.userSpecificRanges")
    String newEntitySettings_userSpecificRanges();

    
    @DefaultMessage("Auto-generated Universally Unique Id (UUID)")
    @Key("newEntitySettings.uuid")
    String newEntitySettings_uuid();

    
    @DefaultMessage("New topic")
    @Key("newTopic")
    String newTopic();

    
    @DefaultMessage("Nothing selected")
    @Key("nothingSelected")
    String nothingSelected();

    
    @DefaultMessage("Open")
    @Key("open")
    String open();

    
    @DefaultMessage("Open in New Window")
    @Key("openInNewWindow")
    String openInNewWindow();

    
    @DefaultMessage("Owned by Me")
    @Key("ownedByMe")
    String ownedByMe();

    
    @DefaultMessage("Owner")
    @Key("owner")
    String owner();

    
    @DefaultMessage("Page")
    @Key("pagination.Page")
    String pagination_Page();

    
    @DefaultMessage("Next")
    @Key("pagination.next")
    String pagination_next();

    
    @DefaultMessage("of")
    @Key("pagination.of")
    String pagination_of();

    
    @DefaultMessage("Prev")
    @Key("pagination.prev")
    String pagination_prev();

    
    @DefaultMessage("Password")
    @Key("password")
    String password();

    
    @DefaultMessage("Password reset error")
    @Key("password.reset.error.generic.msg")
    String password_reset_error_generic_msg();

    
    @DefaultMessage("An error occurred. Please contact the administrator.")
    @Key("password.reset.error.generic.submsg")
    String password_reset_error_generic_submsg();

    
    @DefaultMessage("Invalid email address.")
    @Key("password.reset.error.invalidemail.msg")
    String password_reset_error_invalidemail_msg();

    
    @DefaultMessage("The email address that you supplied is not valid.  Your password has not been reset.")
    @Key("password.reset.error.invalidemail.submsg")
    String password_reset_error_invalidemail_submsg();

    
    @DefaultMessage("Your password has been reset")
    @Key("password.reset.success.msg")
    String password_reset_success_msg();

    
    @DefaultMessage("A temporary password has been sent to your email address.")
    @Key("password.reset.success.submsg")
    String password_reset_success_submsg();

    
    @DefaultMessage("Reset Password")
    @Key("password.resetPassword")
    String password_resetPassword();

    @DefaultMessage("Plus")
    @Key("plus")
    String plus();

    
    @DefaultMessage("Project")
    @Key("project")
    String project();

    
    @DefaultMessage("Project description")
    @Key("projectDescription")
    String projectDescription();

    
    @DefaultMessage("Project Feed")
    @Key("projectFeed")
    String projectFeed();

    
    @DefaultMessage("Project History")
    @Key("projectHistory")
    String projectHistory();

    
    @DefaultMessage("Project name")
    @Key("projectName")
    String projectName();

    
    @DefaultMessage("Please enter a project name")
    @Key("projectNameMissingErrorMessage")
    String projectNameMissingErrorMessage();

    
    @DefaultMessage("Description")
    @Key("projectSettings.description")
    String projectSettings_description();

    @DefaultMessage("Language")
    @Key("projectSettings.language")
    String projectSettings_language();

    @DefaultMessage("Enter a language tag for labelling new entities and to use as the primary display language.  <em>Leave empty for no language tag</em>.")
    @Key("projectSettings.language.helpText")
    SafeHtml projectSettings_language_helpText();

    @DefaultMessage("Any owl:import that cannot be loaded will be silently ignored")
    @Key("projectSettings.owlimport.helpText")
    SafeHtml projectSettings_owl_import_helpText();

    
    @DefaultMessage("Display name")
    @Key("projectSettings.displayName")
    String projectSettings_displayName();


    @DefaultMessage("Project Settings")
    @Key("projectSettings.headerSection.title")
    String projectSettings_headerSection_title();

    
    @DefaultMessage("Main Settings")
    @Key("projectSettings.mainSettings")
    String projectSettings_mainSettings();

    
    @DefaultMessage("Payload URLs")
    @Key("projectSettings.payloadUrls")
    String projectSettings_payloadUrls();

    
    @DefaultMessage("Slack Integration")
    @Key("projectSettings.slackIntegration")
    String projectSettings_slackIntegration();

    
    @DefaultMessage("Slack Webhook URL")
    @Key("projectSettings.slackWebHookUrl")
    String projectSettings_slackWebHookUrl();

    @DefaultMessage("Enter a URL for an incoming Slack Webhook (This is generated by Slack for a particular workspace\n" +
            " and channel.  Please see the Slack Help pages for more information).  When comments are posted on entities Slack users\n" +
            " will also receive notifications via Slack.")
    @Key("projectSettings.slackWebHookHelp")
    String projectSettings_slackWebHookHelp();
    
    @DefaultMessage("Project Settings")
    @Key("projectSettings.title")
    String projectSettings_title();

    @DefaultMessage("Prefixes")
    @Key("prefixes.title")
    String prefixes_title();

    @DefaultMessage("Project Prefixes")
    @Key("prefixes.project.title")
    String prefixes_project_title();

    @DefaultMessage("Prefix name")
    @Key("prefixes.prefixname")
    String prefixes_prefixname();

    @DefaultMessage("Prefix")
    @Key("prefixes.prefix")
    String prefixes_prefix();

    @DefaultMessage("Project Prefixes")
    @Key("prefixes.edit")
    String prefixes_edit();



    
    @DefaultMessage("Web Hooks")
    @Key("projectSettings.webHooks")
    String projectSettings_webHooks();

    
    @DefaultMessage("Projects")
    @Key("projects")
    String projects();

    
    @DefaultMessage("Properties")
    @Key("properties")
    String properties();

    
    @DefaultMessage("Property IRI")
    @Key("propertyIri")
    String propertyIri();

    
    @DefaultMessage("Refresh")
    @Key("refresh")
    String refresh();

    
    @DefaultMessage("Refresh Tree")
    @Key("refreshTree")
    String refreshTree();

    
    @DefaultMessage("Reply")
    @Key("replyToComment")
    String replyToComment();

    
    @DefaultMessage("Search")
    @Key("search")
    String search();

    
    @DefaultMessage("Search for Class")
    @Key("search.class")
    String search_class();

    
    @DefaultMessage("Enter text to search for (multi-word search supported)")
    @Key("search.hint")
    String search_hint();

    @DefaultMessage(
            "Separate search words by spaces to perform multi-word search")
    @Key("search.help")
    String search_help();

    @DefaultMessage("Share")
    @Key("share")
    String share();

    
    @DefaultMessage("Shared with Me")
    @Key("sharedWithMe")
    String sharedWithMe();

    
    @DefaultMessage("Anyone with the link can")
    @Key("sharing.anyoneWithTheLinkCan")
    String sharing_anyoneWithTheLinkCan();

    
    @DefaultMessage("Comment")
    @Key("sharing.comment")
    String sharing_comment();

    
    @DefaultMessage("Edit")
    @Key("sharing.edit")
    String sharing_edit();

    
    @DefaultMessage("Link sharing enabled")
    @Key("sharing.linkSharingEnabled")
    String sharing_linkSharingEnabled();

    
    @DefaultMessage("Manage")
    @Key("sharing.manage")
    String sharing_manage();

    
    @DefaultMessage("Share with specific people")
    @Key("sharing.shareWithSpecificPeople")
    String sharing_shareWithSpecificPeople();

    @DefaultMessage("Loading sharing settings")
    @Key("sharing.settings.loading")
    String sharing_settings_loading();

    @DefaultMessage("Sharing settings")
    @Key("sharing.settings.title")
    String sharing_settings_title();

    @DefaultMessage("Updating sharing settings")
    @Key("sharing.settings.updating")
    String sharing_settings_updating();
    
    @DefaultMessage("Sign-in Required")
    @Key("sharing.signInRequired")
    String sharing_signInRequired();

    
    @DefaultMessage("View")
    @Key("sharing.view")
    String sharing_view();

    
    @DefaultMessage("Show Direct Link")
    @Key("showDirectLink")
    String showDirectLink();

    
    @DefaultMessage("Show IRI")
    @Key("showIri")
    String showIri();

    
    @DefaultMessage("showing")
    @Key("showing")
    String showing();

    
    @DefaultMessage("Sign In")
    @Key("signIn")
    String signIn();

    
    @DefaultMessage("Please sign in to continue")
    @Key("signInToContinue")
    String signInToContinue();

    
    @DefaultMessage("Sign Out")
    @Key("signOut")
    String signOut();

    
    @DefaultMessage("Sign up for account")
    @Key("signUpForAccount")
    String signUpForAccount();

    
    @DefaultMessage("Sort by Entity")
    @Key("sortByEntity")
    String sortByEntity();

    
    @DefaultMessage("Sort by Last Modified")
    @Key("sortByLastModified")
    String sortByLastModified();

    
    @DefaultMessage("Sort by Last Opened")
    @Key("sortByLastOpened")
    String sortByLastOpened();

    
    @DefaultMessage("Sort by Last Updated")
    @Key("sortByLastUpdated")
    String sortByLastUpdated();

    
    @DefaultMessage("Sort by Owner")
    @Key("sortByOwner")
    String sortByOwner();

    @DefaultMessage("Go to entity")
    @Key("navigation.goToEntity")
    String navigation_goToEntity();

    
    @DefaultMessage("Sort by Project Name")
    @Key("sortByProjectName")
    String sortByProjectName();

    
    @DefaultMessage("Start new thread")
    @Key("startNewCommentThread")
    String startNewCommentThread();

    
    @DefaultMessage("days ago")
    @Key("time.daysAgo")
    String time_daysAgo();

    
    @DefaultMessage("hours ago")
    @Key("time.hoursAgo")
    String time_hoursAgo();

    
    @DefaultMessage("Less than one minute ago")
    @Key("time.lessThanOneMinuteAgo")
    String time_lessThanOneMinuteAgo();

    
    @DefaultMessage("minutes ago")
    @Key("time.minutesAgo")
    String time_minutesAgo();

    
    @DefaultMessage("Trash")
    @Key("trash")
    String trash();

    
    @DefaultMessage("Clear pruning")
    @Key("tree.clearPruning")
    String tree_clearPruning();

    
    @DefaultMessage("Prune all branches to root")
    @Key("tree.pruneAllBranchesToRoot")
    String tree_pruneAllBranchesToRoot();

    
    @DefaultMessage("Prune branch to root")
    @Key("tree.pruneBranchToRoot")
    String tree_pruneBranchToRoot();

    
    @DefaultMessage("Apply External Edits")
    @Key("uploadAndMerge")
    String uploadAndMerge();

    @DefaultMessage("Merge Ontologies")
    @Key("uploadAndMergeAdditions")
    String uploadAndMergeAdditions();

    
    @DefaultMessage("Upload Project")
    @Key("uploadProject")
    String uploadProject();

    
    @DefaultMessage("Username")
    @Key("userName")
    String userName();

    
    @DefaultMessage("View Projects")
    @Key("viewProjects")
    String viewProjects();

    
    @DefaultMessage("Watch")
    @Key("watch")
    String watch();

    @DefaultMessage("Merge Into...")
    @Key("merge.mergeInto")
    String merge_mergeInto();

    @DefaultMessage("Merge {0}")
    @Key("merge.mergeEntity")
    String merge_mergeEntity(String typeName);

    @DefaultMessage("Select {0} to Merge Into")
    @Key("merge.selectEntityToMergeInto")
    String merge_selectEntityToMergeInto(String lowerCaseTypeName);


    @DefaultMessage("This operation will merge the selected {0} into another {0}.<br><br>" +
            "Specify a class name in the field below.  The currently selected {0} will be merged into this {0}.")
    @Key("merge.description")
    String merge_description(String lowerCaseTypeName);

    @DefaultMessage("Merge {0}")
    @Key("merge.mergeEntities")
    String merge_mergeEntities(String typePluralName);

    @DefaultMessage("Do you want to merge entities?")
    @Key("merge.confirmMessage")
    String merge_confirmMergeMessage();

    @DefaultMessage("Entity Tags")
    @Key("tags.entityTags")
    String tags_entityTags();

    @DefaultMessage("Tags...")
    @Key("tags.edit")
    String tags_edit();

    @DefaultMessage("Project Tags")
    @Key("tags.projectTagsTitle")
    String tags_projectTagsTitle();

    @DefaultMessage("Delete Tag")
    @Key("tags.deleteTag")
    String tags_deleteTag();

    @DefaultMessage("Displayed tags")
    @Key("tags.displayedTags")
    String tags_displayedTags();

    @DefaultMessage("The tag <strong>{0}</strong> is used to tag <strong>{1,number}&nbsp;entities</strong>.  Are you sure that you want to delete this tag?" +
            "<br><br>" +
            "This operation cannot be undone.")
    @AlternateMessage({
            "one", "The tag <strong>{0}</strong> is used to tag <strong>one entity</strong>.  Are you sure that you want to delete this tag?" +
            "<br><br>" +
            "This operation cannot be undone."})
    @Key("tags.deleteConfirmationMessage")
    String tags_deleteConfirmationMessage(String tagLabel, @PluralCount int usageCount);

    @DefaultMessage("Tags")
    @Key("tags.editProjectTags")
    String tags_EditProjectTags();

    @DefaultMessage("Display tags")
    @Key("tags.displayTags")
    String tags_DisplayTags();

    @DefaultMessage("Duplicate Tag")
    @Key("tags.duplicateTag.title")
    String tags_duplicateTag_Title();

    @DefaultMessage("The label <strong>{0}</strong> is used for more that one tag.<br><br>" +
            "Please edit the labels so that a unique label is used for each tag.")
    String tags_duplicateTag_Message(String label);

    @DefaultMessage("Tag Assignments")
    @Key("tags.tagAssignments.title")
    String tags_tagAssigments_Title();


    @DefaultMessage("Settings")
    @Key("project.settings")
    String projectSettings();

    @DefaultMessage("WebProtégé Application Settings")
    @Key("app.settings.title")
    String applicationSettings();

    @DefaultMessage("System Settings")
    @Key("app.settings.systemSettings")
    String applicationSettings_SystemSettings();

    @DefaultMessage("Application URL")
    @Key("app.settings.applicationUrl")
    String applicationSettings_ApplicationUrl();

    @DefaultMessage("Permissions")
    @Key("app.settings.globalPermissions")
    String applicationSettings_GlobalPermissions();

    @DefaultMessage("Email Settings")
    @Key("app.settings.emailNotificationSettings")
    String applicationSettings_EmailNotifications();

    @DefaultMessage("Scheme")
    @Key("app.settings.url.scheme")
    String applicationSettings_url_scheme();

    @DefaultMessage("Host")
    @Key("app.settings.url.host")
    String applicationSettings_url_host();

    @DefaultMessage("Path")
    @Key("app.settings.url.path")
    String applicationSettings_url_path();

    @DefaultMessage("Port")
    @Key("app.settings.url.port")
    String applicationSettings_url_port();

    @DefaultMessage("New Entity Language Settings")
    @Key("language.defaultSettings.title")
    String language_defaultSettings_title();

    @DefaultMessage("Display Name Settings")
    @Key("displayName.settings.project.title")
    String displayName_settings_project_title();

    @DefaultMessage("Specify how entity display names should be generated.  " +
            "The display name for an entity will be chosen based the ordering below.  Display names can be based on annotation properties and language pairs etc." +
            "Items that appear closer to the top of the list are given priority over " +
            "items that appear closer to the bottom of the list.")
    @Key("displayName.settings.project.helpText")
    String displayName_settings_project_helpText();

    @DefaultMessage("Display name property and language priority")
    @Key("displayName.settings.project.priorityList.title")
    String displayName_settings_project_priorityList_title();

    @DefaultMessage("Reset to referenced languages")
    @Key("displayName.settings.project.reset.title")
    String displayName_settings_project_reset_title();


    @DefaultMessage("Primary Display Name")
    @Key("displayName.settings.local.primary.title")
    String displayName_settings_local_primary_title();

    @DefaultMessage("The primary display name is used as the display name for entities throughout the user interface.  " +
            "Specify a list of property language pairs that should be used to generate the display name.  " +
            "This setting is a local setting that takes precedence over the project default. " +
            "Leave blank to use the project default.")
    @Key("displayName.settings.local.primary.helpText")
    String displayName_settings_local_primary_helpText();

    @DefaultMessage("Secondary Display Name")
    @Key("displayName.settings.local.secondary.title")
    String displayName_settings_local_secondary_title();

    @DefaultMessage("The secondary display name is displayed alongside the primary display name in hierarchies " +
            "and lists of entities.  Specify a list of property language pairs that should be used to generate the " +
            "secondary display name.  Leave blank for no secondary display name.")
    @Key("displayName.settings.local.secondary.helpText")
    String displayName_settings_local_secondary_helpText();


    @DefaultMessage("<em>Note: The current display name settings are not configured to display the language tag <strong>{0}</strong></em><br>" +
            "<br>" +
            "You may continue and change the display name settings afterwards.  To edit the local display name settings please use the Display menu. " +
            "To edit the global display name settings please see the Project Settings page.")
    SafeHtml displayName_noDisplayNameForLangTag(String langTag);

    @DefaultMessage("The {0} {1} does not have a primary display name under the current display name settings")
    @Key("displayName.no_display_name.helpText")
    String displayName_noDisplayName_helpText(String typeName, String defDisplayName);

    @DefaultMessage("Move up")
    @Key("list.moveUp")
    String list_moveUp();

    @DefaultMessage("Move down")
    @Key("list.moveDown")
    String list_moveDown();

    @DefaultMessage("Jump to parent")
    @Key("hierarchy.moveToParent")
    String moveToParent();

    @DefaultMessage("Jump to sibling")
    @Key("hierarchy.moveToSibling")
    String moveToSibling();

    @DefaultMessage("Jump to child")
    @Key("hierarchy.moveToChild")
    String moveToChild();

    @DefaultMessage("Siblings")
    @Key("hierarchy.siblings")
    String hierarchy_siblings();

    @DefaultMessage("Children")
    @Key("hierarchy.children")
    String hierarchy_children();

    @DefaultMessage("Parents")
    @Key("hierarchy.parents")
    String hierarchy_parents();

    @DefaultMessage("Watches")
    @Key("watch.watches")
    String watch_watches();

    @DefaultMessage("Edit Comment")
    @Key("comments.editComment")
    String comments_editComment();

    @DefaultMessage("Search for {0}")
    @Key("search.searchFor")
    String searchFor(String entityType);

    @DefaultMessage("Password changed")
    @Key("password.change.passwordChanged.title")
    String password_change_passwordChanged_Title();

    @DefaultMessage("Your password has been successfully changed.")
    @Key("password.change.passwordChanged.body")
    String password_change_passwordChanged_Body();

    @DefaultMessage("Please specify a new password")
    @Key("password.change.specifyNewPassword")
    String password_change_specifyNewPassword();

    @DefaultMessage("Passwords do not match")
    @Key("password.change.passwordsDoNotMatch.title")
    String password_change_passwordsDoNotMatch_Title();

    @DefaultMessage("Please re-enter the new password and confirmation.")
    @Key("password.change.passwordsDoNotMatch.body")
    String password_change_passwordsDoNotMatch_Body();

    @DefaultMessage("Current password is incorrect")
    @Key("password.change.currentPasswordIncorrect.title")
    String password_change_currentPasswordIncorrect_Title();

    @DefaultMessage("The password that you specified as your current password is incorrect. Please re-enter your current password and try again.")
    @Key("password.change.currentPasswordIncorrect.body")
    String password_change_currentPasswordIncorrect_Body();

    @DefaultMessage("({0,number} rows in total)")
    @AlternateMessage({"one", "(1 row in total)"})
    @Key("pagination.rows")
    String pagination_rows(long elementCount);

    @DefaultMessage("Local name")
    @Key("dictionaryLanguage.localName")
    String dicationaryLanguage_localname();

    @DefaultMessage("OBO Id")
    @Key("dictionaryLanguage.oboId")
    String dictionaryLanguage_oboId();

    @DefaultMessage("Prefixed name")
    @Key("dictionaryLanguage.prefixedName")
    String dictionaryLanguage_prefixedName();

    @DefaultMessage("Import Project Settings")
    @Key("projectSettings.importSettings.title")
    String projectSettings_importSettings_title();

    @DefaultMessage("Paste the project settings that you wish to import into the text area below.  These settings will replaced any existing settings.")
    @Key("projectSettings.importSettings.message")
    String projectSettings_importSettings_message();

    @DefaultMessage("Import Project Settings Error")
    @Key("projectSettings.importSettings.error.title")
    String projectSettings_importSettings_error_title();

    @DefaultMessage("An error occurred when importing the project settings")
    @Key("projectSettings.importSettings.error.message")
    String projectSettings_importSettings_error_message();

    @DefaultMessage("Export settings...")
    @Key("settings.export")
    String settings_export();

    @DefaultMessage("Import settings...")
    @Key("settings.import")
    String settings_import();

    @DefaultMessage("Import settings")
    @Key("settings.import.complete.title")
    String settings_importSettings_complete_title();

    @DefaultMessage("The settings have successfuly been imported.  We recommend that your refresh your browser.")
    @Key("settings.import.complete.message")
    String settings_importSettings_complete_message();


    @DefaultMessage("Add blank tab")
    @Key("perspective.addBlankTab")
    String perspective_addBlankTab();


    @DefaultMessage("Add view")
    @Key("perspective.addView")
    String perspective_addView();


    @DefaultMessage("Close")
    @Key("perspective.close")
    String perspective_close();


    @DefaultMessage("Reset")
    @Key("perspective.reset")
    String perspective_reset();


    @DefaultMessage("Manage...")
    @Key("perspective.manage")
    String perspective_manage();

    @DefaultMessage("Delete {0} tab?")
    @Key("perspective.deleteConfirmation.title")
    String perspective_deleteConfirmation_title(String object);

    @DefaultMessage("Are you sure that you want to permanently delete the <strong>{0}</strong> tab?  This cannot be undone.")
    @Key("perspective.deleteConfirmation.message")
    String perspective_deleteConfirmation_message(String object);

    @DefaultMessage("Reset tabs?")
    @Key("perspective.resetConfirmation.title")
    String perspective_resetConfirmation_title();

    @DefaultMessage("Are you sure that you want to reset the tabs to the default set of tabs?  This cannot be undone.")
    @Key("perspective.resetConfirmation.message")
    String perspective_resetConfirmation_message();
}

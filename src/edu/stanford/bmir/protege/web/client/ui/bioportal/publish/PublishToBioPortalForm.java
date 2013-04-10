package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.*;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class PublishToBioPortalForm extends FlowPanel {


    private static final int VERIFICATION_CHECK_DELAY_MS = 500;
    
    private static final String INCORRECT_USER_NAME_OR_PASSWORD = "Incorrect user name or password";

    public static final String ERROR_FOREGROUND = "web-protege-red-foreground";

    private final TextBox versionNumberTextBox;

    private final TextBoxBase ontologyNameTextBox;

    private final TextBox contactNameTextBox;

    private final TextBox ontologyAcroymnTextBox;

    private final TextArea descriptionTextArea;

    private final WebProtegeDialogForm form;

    private final TextBox homepageTextBox;

    private final TextBox documentationPageTextBox;

    private final TextBox publicationLinkTextBox;

    private final TextBox contactEmailTextBox;

    private final TextBox bioportalUserNameTextBox;

    private final PasswordTextBox bioportalPasswordTextBox;

    private BioPortalUserInfo bioPortalUserInfo;

    private BioPortalOntologyInfo matchingBioPortalOntologyInfo;

    private final BioPortalUserIdOntologiesOracle ontologySuggestOracle;
    
    private FocusWidgetGroup userIdVerifiedWidgets = new FocusWidgetGroup();

    private final Timer verificationCheckTimer;

    private final RadioButton firstVersionRadioBox;

    private final RadioButton updatedVersionRadioBox;

//    private final Label ontologyIdLabel;
    
    private final Label incorrectUserNameOrPasswordLabel;

    public PublishToBioPortalForm(ProjectData projectData, UserDetails userDetails) {

        verificationCheckTimer = new Timer() {
            @Override
            public void run() {
                validateBioPortalUser();
            }
        };
        form = new WebProtegeDialogForm();
        add(form);

        bioportalUserNameTextBox = form.addTextBox("Sign in", "BioPortal user name", userDetails.getUserId().getUserName(), new NonEmptyWebProtegeDialogTextFieldValidator("Please specify a use name"));
        bioportalUserNameTextBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                validateBioPortalUser();
            }
        });
        bioportalUserNameTextBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                scheduleUserIdVerificationCheck();
            }
        });
        
        bioportalPasswordTextBox = form.addPasswordTextBox("", "BioPortal password", "");
        bioportalPasswordTextBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                validateBioPortalUser();     
            }
        });
        bioportalPasswordTextBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                scheduleUserIdVerificationCheck();
            }
        });

        incorrectUserNameOrPasswordLabel = new InlineLabel(INCORRECT_USER_NAME_OR_PASSWORD);
        incorrectUserNameOrPasswordLabel.addStyleName(ERROR_FOREGROUND);
        form.addWidget("", incorrectUserNameOrPasswordLabel);
        incorrectUserNameOrPasswordLabel.setVisible(false);

        form.addVerticalSpacer();


        firstVersionRadioBox = form.addRadioButtonAnnotated("Publish as", "PublishType", "The first version of this ontology", "There is <b>no prior version</b> of this ontology in BioPortal.");
        firstVersionRadioBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                handlePublicationTypeChange(PublicationType.FIRST_VERSION);
            }
        });
        
        updatedVersionRadioBox = form.addRadioButtonAnnotated("Publish as", "PublishType", "An update of a previously published version of this ontology", "A prior version of this ontology has already been published to BioPortal.<br>This is an <b>updated version</b> of that ontology.");
        updatedVersionRadioBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                handlePublicationTypeChange(PublicationType.UPDATED_VERSION);
            }
        });

        
        
        form.addVerticalSpacer();

        
        ontologySuggestOracle = new BioPortalUserIdOntologiesOracle();
        SuggestBox suggestBox = form.addSuggestBox("Ontology name", "Name of ontology that will be shown in BioPortal", "", ontologySuggestOracle, new NonEmptyWebProtegeDialogTextFieldValidator("Please enter an ontology name"));
        suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                BioPortalOntologyInfoSuggestion suggestion = (BioPortalOntologyInfoSuggestion) suggestionSelectionEvent.getSelectedItem();
                setMatchingBioPortalOntology(suggestion.getOntologyInfo());
            }
        });
        suggestBox.setAutoSelectEnabled(false);
        form.addWidget("Ontology name", suggestBox);

        ontologyNameTextBox = suggestBox.getTextBox();
        ontologyNameTextBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                handleOntologyNameChange();
            }
        });
        ontologyNameTextBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                handleOntologyNameChange();
            }
        });

//        ontologyIdLabel = new Label();
//        form.addWidget("", ontologyIdLabel);


        ProjectId projectId = ProjectId.get(projectData.getName());
        ontologyAcroymnTextBox = form.addTextBox("Acronym", "Maximum 16 characters, no spaces, all upper case", projectId.getSuggestedAcronym(), new NonEmptyWebProtegeDialogTextFieldValidator("Please specify an acronym"));
        versionNumberTextBox = form.addTextBox("Version", "e.g. 1.0.3", "", new NonEmptyWebProtegeDialogTextFieldValidator("Please specify a version string"));

        form.addVerticalSpacer();

        descriptionTextArea = new TextArea();
        descriptionTextArea.setCharacterWidth(WebProtegeDialogForm.DEFAULT_TEXT_BOX_VISIBILE_LENGTH);
        form.addWidget("Description", descriptionTextArea);
//        descriptionTextArea.setText(projectData.getDescription());

        form.addVerticalSpacer();


        contactNameTextBox = form.addTextBox("Contact name", "A human readable contact name (displayed on the BioPortal website)", userDetails.getDisplayName(), new NonEmptyWebProtegeDialogTextFieldValidator("Please specify a contact name"));
        contactEmailTextBox = form.addTextBox("email", "", userDetails.getEmailAddress().or(""), new NonEmptyWebProtegeDialogTextFieldValidator("Please specify an email address"));

        form.addVerticalSpacer();

        homepageTextBox = form.addTextBox("Homepage link", "A link to the project homepage", "", NullWebProtegeDialogTextFieldValidator.get());
        documentationPageTextBox = form.addTextBox("Documentation link", "A link to a web page containing documentation", "", NullWebProtegeDialogTextFieldValidator.get());
        publicationLinkTextBox = form.addTextBox("Publication link", "A link to a publication about the ontology", "", NullWebProtegeDialogTextFieldValidator.get());



        
        userIdVerifiedWidgets.add(firstVersionRadioBox);
        userIdVerifiedWidgets.add(updatedVersionRadioBox);
        userIdVerifiedWidgets.add(contactNameTextBox);
        userIdVerifiedWidgets.add(contactEmailTextBox);
        userIdVerifiedWidgets.add(ontologyNameTextBox);
        userIdVerifiedWidgets.add(ontologyAcroymnTextBox);
        userIdVerifiedWidgets.add(versionNumberTextBox);
        userIdVerifiedWidgets.add(descriptionTextArea);
        userIdVerifiedWidgets.add(homepageTextBox);
        userIdVerifiedWidgets.add(documentationPageTextBox);
        userIdVerifiedWidgets.add(publicationLinkTextBox);

        disableUserIdVerifiedWidgets();

        setPublicationType(PublicationType.FIRST_VERSION);
    }

    public List<WebProtegeDialogValidator> getValidators() {
        return form.getDialogValidators();
    }
    
    private void scheduleUserIdVerificationCheck() {
        GWT.log("Scheduling userId verification");
        verificationCheckTimer.cancel();
        verificationCheckTimer.schedule(VERIFICATION_CHECK_DELAY_MS);
    }

    private void disableUserIdVerifiedWidgets() {
        userIdVerifiedWidgets.setWidgetsEnabled(false);
    }


    private void validateBioPortalUser() {
        if (bioPortalUserInfo == null) {
            incorrectUserNameOrPasswordLabel.setVisible(true);
            disableUserIdVerifiedWidgets();
        }
        else {
            incorrectUserNameOrPasswordLabel.setVisible(false);
        }
        String bioPortalUserName = getBioPortalUserName();
        String bioPortalPassword = getBioPortalPassword();
        if(!bioPortalUserName.isEmpty() && !bioPortalPassword.isEmpty()) {
            validateBioPortalUserCredentials(bioPortalUserName, bioPortalPassword);
        }
    }

    /**
     * Validates the specified BioPortal userName and password.  If valid, the widgets in the userIdVerifiedWidgets
     * are set enabled and the other user fields (e.g. contact information) are filled out based on the data obtained
     * from BioPortal.  If invalid, the widgets in the userIdVerifiedWidgets are set disabled.
     * @param bioPortalUserName The BioPortal userName.  Not <code>null</code>.
     * @param bioPortalPassword The BioPortal userPassword.  Not <code>null</code>.
     */
    private void validateBioPortalUserCredentials(String bioPortalUserName, String bioPortalPassword) {
        if(bioPortalUserName == null) {
            throw new RuntimeException("bioPortalUserName must not be null");
        }
        if(bioPortalPassword == null) {
            throw new RuntimeException("bioPortalPassword must not be null");
        }
        GWT.log("Validating user credentials");
        BioPortalAPIServiceAsync service = BioPortalAPIServiceManager.getServiceAsync();
        service.getBioPortalUserInfo(bioPortalUserName, bioPortalPassword, new AsyncCallback<BioPortalUserInfo>() {
            public void onFailure(Throwable caught) {
                if(caught instanceof CannotValidateBioPortalCredentials) {
                    GWT.log("Invalid bioportal credentials");
                    updateFormWithBioPortalUserInfo(null);
                }
            }

            public void onSuccess(BioPortalUserInfo result) {
                GWT.log("Validated user");
                updateFormWithBioPortalUserInfo(result);
            }
        });
    }

    public PublishToBioPortalInfo getData() {
        BioPortalOntologyId ontologyId = matchingBioPortalOntologyInfo == null ? BioPortalOntologyId.getNull() : matchingBioPortalOntologyInfo.getOntologyId();
        return new PublishToBioPortalInfo(bioPortalUserInfo.getUserId(),
                ontologyId,
                getOntologyName(), getOntologyAcronym(),
                descriptionTextArea.getText().trim(),
                contactNameTextBox.getText().trim(),
                contactEmailTextBox.getText().trim(),
                versionNumberTextBox.getText().trim(),
                homepageTextBox.getText().trim(),
                documentationPageTextBox.getText().trim(),
                publicationLinkTextBox.getText().trim());
    }

    private String getBioPortalPassword() {
        return bioportalPasswordTextBox.getText().trim();
    }

    private String getBioPortalUserName() {
        return bioportalUserNameTextBox.getText().trim();
    }

    public Focusable getInitialFocusable() {
        return form.getInitialFocusable();
    }

    private void updateFormWithBioPortalUserInfo(BioPortalUserInfo info) {
        userIdVerifiedWidgets.setWidgetsEnabled(info != null);
        incorrectUserNameOrPasswordLabel.setVisible(info == null);

        if(info == null) {
            return;
        }

        contactNameTextBox.setText(info.getFirstName() + " " + info.getLastName());
        contactEmailTextBox.setText(info.getEmail());
        bioPortalUserInfo = info;

        ontologySuggestOracle.setBioPortalUserId(info.getUserId());
    }
    
    public void handlePublicationTypeChange(PublicationType publicationType) {
        if(publicationType == PublicationType.FIRST_VERSION) {
            ontologySuggestOracle.setEnabled(false);
        }
        else {
            ontologySuggestOracle.setEnabled(true);
        }
        matchingBioPortalOntologyInfo = null;
        handleOntologyNameChange();
    }

    public void setPublicationType(PublicationType publicationType) {
        if(publicationType == PublicationType.FIRST_VERSION) {
            firstVersionRadioBox.setValue(true);
        }
        else {
            updatedVersionRadioBox.setValue(true);
        }
        handlePublicationTypeChange(publicationType);
    }
    
    private void handleOntologyNameChange() {
        BioPortalAPIServiceAsync service = BioPortalAPIServiceManager.getServiceAsync();
        service.getBioPortalOntologyInfoForDisplayName(getOntologyName(), new AsyncCallback<BioPortalOntologyInfo>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(BioPortalOntologyInfo result) {
                setMatchingBioPortalOntology(result);
            }
        });
    }


    private void setMatchingBioPortalOntology(BioPortalOntologyInfo result) {
        matchingBioPortalOntologyInfo = result;
        setVersionPlaceholderToMatchBioPortalOntologyInfo();

        if (matchingBioPortalOntologyInfo != null) {
            ontologyAcroymnTextBox.setText(matchingBioPortalOntologyInfo.getAbbreviation());
            descriptionTextArea.setText(matchingBioPortalOntologyInfo.getDescription());
        }

        if(firstVersionRadioBox.getValue()) {
            if(matchingBioPortalOntologyInfo != null) {
                ontologyNameTextBox.addStyleName(ERROR_FOREGROUND);
            }
            else {
                ontologyNameTextBox.removeStyleName(ERROR_FOREGROUND);
            }
        }
        else {
            if(matchingBioPortalOntologyInfo == null) {
                ontologyNameTextBox.addStyleName(ERROR_FOREGROUND);
            }
            else {
                ontologyNameTextBox.removeStyleName(ERROR_FOREGROUND);
            }
        }
    }

    private void setVersionPlaceholderToMatchBioPortalOntologyInfo() {
        if(matchingBioPortalOntologyInfo == null) {
            versionNumberTextBox.getElement().setAttribute("placeholder", "(New ontology, enter version number e.g. 1.0.0)");
        }
        else {
            versionNumberTextBox.getElement().setAttribute("placeholder", "(Previous version: " + matchingBioPortalOntologyInfo.getVersionNumber() + ")");
        }

    }

    private String getOntologyName() {
        return ontologyNameTextBox.getText().trim();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setupValidators() {
        WebProtegeDialogValidator userIdValidator = new WebProtegeDialogValidator() {
            public ValidationState getValidationState() {
                return bioPortalUserInfo != null ? ValidationState.VALID : ValidationState.INVALID;
            }

            public String getValidationMessage() {
                return "Either the BioPortal username or password (or both) is incorrect";
            }
        };
        form.addDialogValidator(userIdValidator);

        WebProtegeDialogValidator firstVersionValidator = new WebProtegeDialogValidator() {
            private String msg;
            public ValidationState getValidationState() {

                if(matchingBioPortalOntologyInfo != null) {
                    if(isOntologyNameAlreadyPublished()) {
                        msg = "There is already an ontology named " + getOntologyName() + " in BioPortal";
                        return ValidationState.INVALID;
                    }
                    if(isOntologyAcronymAlreadPublished()) {
                        msg = "There is already an ontology with the acronym " + getOntologyAcronym() + " in BioPortal";
                        return ValidationState.INVALID;
                    }
                }
                return ValidationState.VALID;
            }

            public String getValidationMessage() {
                return msg;
            }
        };
        form.addDialogValidator(firstVersionValidator);

        WebProtegeDialogValidator updatedVersionValidator = new WebProtegeDialogValidator() {
            public ValidationState getValidationState() {
                if(firstVersionRadioBox.getValue()) {
                    return ValidationState.VALID;
                }
                return matchingBioPortalOntologyInfo != null ? ValidationState.VALID : ValidationState.INVALID;
            }

            public String getValidationMessage() {
                if(matchingBioPortalOntologyInfo == null) {
                    return "The combination of the supplied ontology name and abbreviation do not correspond to any existing ontology in BioPortal";
                }
                if(!isOntologyNameAlreadyPublished()) {
                    return "The specified ontology name does not correspond to the name of any existing ontology in BioPortal";
                }
                if(!isOntologyAcronymAlreadPublished()) {
                    return "The specified ontology acronym does not correspond to the acronym of any existing ontology in BioPortal";
                }
                return "";
            }
        };
        form.addDialogValidator(updatedVersionValidator);


    }

    private boolean isOntologyNameSupplied() {
        return !getOntologyName().isEmpty();
    }

    private boolean isOntologyAcronymSupplied() {
        return !getOntologyAcronym().isEmpty();
    }

    private boolean isOntologyAcronymAlreadPublished() {
        return getOntologyAcronym().equals(matchingBioPortalOntologyInfo.getAbbreviation());
    }

    private boolean isOntologyNameAlreadyPublished() {
        return getOntologyName().equals(matchingBioPortalOntologyInfo.getDisplayLabel());
    }

    private String getOntologyAcronym() {
        return ontologyAcroymnTextBox.getText().trim();
    }
}

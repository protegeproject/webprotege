package edu.stanford.bmir.protege.web.client.ui.bioportal.publish;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.*;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.FocusWidgetGroup;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class PublishToBioPortalForm extends FlowPanel {


    private static final int VERIFICATION_CHECK_DELAY_MS = 500;

    private final TextBox versionNumberTextBox;

    private final TextBox ontologyNameTextBox;

    private final TextBox contactNameTextBox;

    private final TextBox ontologyAbbreviationTextBox;

    private final TextArea descriptionTextArea;

    private final WebProtegeDialogForm form;

    private final TextBox homepageTextBox;

    private final TextBox documentationPageTextBox;

    private final TextBox publicationLinkTextBox;

    private final TextBox contactEmailTextBox;

    private final TextBox bioportalUserNameTextBox;

    private final PasswordTextBox bioportalPasswordTextBox;

    private BioPortalUserInfo bioPortalUserInfo;

    private BioPortalOntologyInfo bioportalOntologyInfo;

    private final BioPortalUserIdOntologiesOracle ontologySuggestOracle;
    
    private FocusWidgetGroup userIdVerifiedWidgets = new FocusWidgetGroup();

    private final Timer verificationCheckTimer;

    public PublishToBioPortalForm(ProjectData projectData, UserData userData) {

        verificationCheckTimer = new Timer() {
            @Override
            public void run() {
                validateBioPortalUser();
            }
        };
        form = new WebProtegeDialogForm();
        add(form);

        bioportalUserNameTextBox = form.addTextBox("BioPortal user name", "User name", userData.getName());
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
        
        bioportalPasswordTextBox = form.addPasswordTextBox("BioPortal password", "Enter BioPortal user password", "");
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
        
        form.addVerticalSpacer();
        
        contactNameTextBox = form.addTextBox("Contact name", "A human readable contact name (displayed on the BioPortal website)", userData.getName());
        contactEmailTextBox = form.addTextBox("Contact email", "", userData.getEmail());


        form.addVerticalSpacer();

        ontologySuggestOracle = new BioPortalUserIdOntologiesOracle();
        ontologyNameTextBox = new TextBox();
        ontologyNameTextBox.setVisibleLength(WebProtegeDialogForm.DEFAULT_TEXT_BOX_VISIBILE_LENGTH);
        SuggestBox suggestBox = new SuggestBox(ontologySuggestOracle, ontologyNameTextBox);
        suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                BioPortalOntologyInfoSuggestion suggestion = (BioPortalOntologyInfoSuggestion) suggestionSelectionEvent.getSelectedItem();
                setMatchingBioPortalOntology(suggestion.getOntologyInfo());
            }
        });
        suggestBox.setAutoSelectEnabled(false);
        form.addWidget("Ontology name", suggestBox);

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


        ProjectId projectId = new ProjectId(projectData.getName());
        ontologyAbbreviationTextBox = form.addTextBox("Ontology abbreviation", "Maximum 16 characters, no spaces, all upper case", projectId.getSuggestedAcronym());
        versionNumberTextBox = form.addTextBox("Ontology version", "e.g. 1.0.3", "");

        form.addVerticalSpacer();

        descriptionTextArea = new TextArea();
        descriptionTextArea.setCharacterWidth(WebProtegeDialogForm.DEFAULT_TEXT_BOX_VISIBILE_LENGTH);
        form.addWidget("Description", descriptionTextArea);
//        descriptionTextArea.setText(projectData.getDescription());

        form.addVerticalSpacer();

        homepageTextBox = form.addTextBox("Homepage", "A link to the project homepage", "");
        documentationPageTextBox = form.addTextBox("Documentation page", "A link to a web page containing documentation", "");
        publicationLinkTextBox = form.addTextBox("Publication link", "A link to a publication about the ontology", "");
        
        
        userIdVerifiedWidgets.add(contactNameTextBox);
        userIdVerifiedWidgets.add(contactEmailTextBox);
        userIdVerifiedWidgets.add(ontologyNameTextBox);
        userIdVerifiedWidgets.add(ontologyAbbreviationTextBox);
        userIdVerifiedWidgets.add(versionNumberTextBox);
        userIdVerifiedWidgets.add(descriptionTextArea);
        userIdVerifiedWidgets.add(homepageTextBox);
        userIdVerifiedWidgets.add(documentationPageTextBox);
        userIdVerifiedWidgets.add(publicationLinkTextBox);

        disableUserIdVerifiedWidgets();
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
            disableUserIdVerifiedWidgets();
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
                }
            }

            public void onSuccess(BioPortalUserInfo result) {
                GWT.log("Validated user");
                updateFormWithBioPortalUserInfo(result);
            }
        });
    }

    public PublishToBioPortalInfo getData() {
        BioPortalOntologyId ontologyId = bioportalOntologyInfo == null ? BioPortalOntologyId.getNull() : bioportalOntologyInfo.getOntologyId();
        return new PublishToBioPortalInfo(bioPortalUserInfo.getUserId(),
                ontologyId,
                getOntologyDisplayName(),
                ontologyAbbreviationTextBox.getText().trim(),
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
        if(info == null) {
            return;
        }

        contactNameTextBox.setText(info.getFirstName() + " " + info.getLastName());
        contactEmailTextBox.setText(info.getEmail());
        bioPortalUserInfo = info;

        ontologySuggestOracle.setBioPortalUserId(info.getUserId());
    }
    
    private void handleOntologyNameChange() {
        if(bioportalOntologyInfo != null && getOntologyDisplayName().equals(bioportalOntologyInfo.getDisplayLabel())) {
            return;
        }
        BioPortalAPIServiceAsync service = BioPortalAPIServiceManager.getServiceAsync();
        service.getBioPortalOntologyInfoForDisplayName(getOntologyDisplayName(), new AsyncCallback<BioPortalOntologyInfo>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(BioPortalOntologyInfo result) {
                setMatchingBioPortalOntology(result);
            }
        });
    }


    private void setMatchingBioPortalOntology(BioPortalOntologyInfo result) {
        bioportalOntologyInfo = result;
        setVersionPlaceholderToMatchBioPortalOntologyInfo();
        if (bioportalOntologyInfo != null) {
            ontologyAbbreviationTextBox.setText(bioportalOntologyInfo.getAbbreviation());
            descriptionTextArea.setText(bioportalOntologyInfo.getDescription());
        }
    }

    private void setVersionPlaceholderToMatchBioPortalOntologyInfo() {
        if(bioportalOntologyInfo == null) {
            versionNumberTextBox.getElement().setAttribute("placeholder", "(New ontology, enter version number e.g. 1.0.0)");
        }
        else {
            versionNumberTextBox.getElement().setAttribute("placeholder", "(Previous version: " + bioportalOntologyInfo.getVersionNumber() + ")");
        }

    }

    private String getOntologyDisplayName() {
        return ontologyNameTextBox.getText().trim();
    }
}

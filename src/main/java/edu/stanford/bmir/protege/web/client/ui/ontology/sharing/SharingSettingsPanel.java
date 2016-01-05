package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.itemlist.ItemListSuggestBox;
import edu.stanford.bmir.protege.web.client.itemlist.ItemListSuggestOracle;
import edu.stanford.bmir.protege.web.client.itemlist.PersonIdItemListSuggestionOracle;
import edu.stanford.bmir.protege.web.client.itemlist.ValueBoxCursorPositionProvider;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.sharing.*;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class SharingSettingsPanel extends WebProtegeDialogForm {

    private final DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;
    
    private final SharingSettingsList sharingSettingsList;
    
    private final SharingSettingsDefaultSharingSettingPanel defaultSharingSettingPanel;
    
    public static final String PLACE_HOLDER_TEXT = "Enter names (1 per line)";

    @Inject
    public SharingSettingsPanel(final ProjectId projectId, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;

        defaultSharingSettingPanel = new SharingSettingsDefaultSharingSettingPanel();
        add(defaultSharingSettingPanel);

        WebProtegeLabel listLabel = new WebProtegeLabel("Who has access");
        add(listLabel);
        listLabel.addStyleName("web-protege-header-label");


        sharingSettingsList = new SharingSettingsList(loggedInUserProvider);
        
        refillSharingSettingsList(projectId);

        ScrollPanel scrollPanel = new ScrollPanel(sharingSettingsList);
        scrollPanel.setAlwaysShowScrollBars(false);
        scrollPanel.setHeight("200px");
        add(scrollPanel);


        final TextArea addPeopleTextArea = new TextArea();
        addPeopleTextArea.setVisibleLines(5);
        addPeopleTextArea.setCharacterWidth(50);
        addPeopleTextArea.getElement().setAttribute("placeholder", PLACE_HOLDER_TEXT);

        final SuggestBox suggestBox = new ItemListSuggestBox<>(new PersonIdItemListSuggestionOracle(addPeopleTextArea, dispatchServiceManager), addPeopleTextArea);


        FlowPanel addPeoplePanel = new FlowPanel();
        addPeoplePanel.addStyleName("web-protege-sharing-add-people");

        FlowPanel addPeopleLabelListHolder = new FlowPanel();
        InlineLabel addPeopleLabel = new InlineLabel("Add people who ");
        addPeopleLabel.addStyleName("web-protege-header-label");
        addPeopleLabelListHolder.add(addPeopleLabel);
        
        final SharingSettingsDropDown lb = new SharingSettingsDropDown();
        addPeopleLabelListHolder.add(lb);
        addPeoplePanel.add(addPeopleLabelListHolder);
        
        addPeoplePanel.add(suggestBox);
        Button add = new Button("Add", new ClickHandler() {
            public void onClick(ClickEvent event) {
                handleAdd(suggestBox, lb, addPeopleTextArea);
            }   
        });
        add.addStyleName("button-style");
        addPeoplePanel.add(add);
        

        add(addPeoplePanel);
    }

    private void handleAdd(SuggestBox suggestBox, final SharingSettingsDropDown lb, final TextArea addPeopleTextArea) {

        String suggestBoxText = suggestBox.getText();
        final String [] names = suggestBoxText.trim().split("\n");
        final List<String> itemNames = Arrays.asList(names);

        final SharingPermission sharingPermission = lb.getSelectedItem();
        dispatchServiceManager.execute(new GetPersonIdItemsAction(itemNames), new DispatchServiceCallbackWithProgressDisplay<GetPersonIdItemsResult>() {
            @Override
            public String getProgressDisplayTitle() {
                return "Adding users";
            }

            @Override
            public String getProgressDisplayMessage() {
                return "Adding users to sharing settings";
            }

            @Override
            public void handleSuccess(GetPersonIdItemsResult result) {
                GWT.log("Retrieved PersonIds: " + result);
                addPersonsToSharingSettings(result, itemNames, sharingPermission, addPeopleTextArea);
            }
        });
    }

    private void addPersonsToSharingSettings(GetPersonIdItemsResult result, List<String> itemNames, SharingPermission sharingPermission, TextArea addPeopleTextArea) {
        List<PersonId> personIds = result.getItems();
        List<String> remainingItemNames = new ArrayList<>(itemNames);
        for(PersonId personId : personIds) {
            remainingItemNames.remove(personId.getId());
        }
        personIds.removeAll(getUsersInSharingSettingsList());
        List<SharingSetting> listDataItems = new ArrayList<>(sharingSettingsList.getListData());
        for(PersonId personId : personIds) {
            listDataItems.add(new SharingSetting(personId, sharingPermission));
        }
        StringBuilder remainingPersonNameList = new StringBuilder();
        for(String remainingItemName : remainingItemNames) {
            remainingPersonNameList.append(remainingItemName);
            remainingPersonNameList.append("\n");
        }
        addPeopleTextArea.setText(remainingPersonNameList.toString().trim());
        sharingSettingsList.setListData(listDataItems);
        if(!remainingItemNames.isEmpty()) {
            String remainingPersonListHtml = remainingPersonNameList.toString().replace("\n", "<br>");
            MessageBox.showMessage(
                    "Unable to share the project with the following people",
                    "<div style=\"margin-left: 20px; margin-top: 10px; margin-bottom: 10px; line-height: 20px; color: maroon;\">"
                            + remainingPersonListHtml
                            + "</div>" + "The people above do not have accounts with WebProtégé.  " +
                            "Projects can only be shared with people who have WebProtégé accounts."
            );
        }
    }

    private List<PersonId> getUsersInSharingSettingsList() {
        List<PersonId> result = new ArrayList<>();
        for(SharingSetting item : sharingSettingsList.getListData()) {
            result.add(item.getPersonId());
        }
        return result;
    }

    private void refillSharingSettingsList(final ProjectId projectId) {
        dispatchServiceManager.execute(new GetProjectSharingSettingsAction(projectId), new DispatchServiceCallback<GetProjectSharingSettingsResult>() {
            @Override
            public void handleSuccess(GetProjectSharingSettingsResult result) {
                updateListData(result.getProjectSharingSettings());
            }
        });
    }

    private void updateListData(ProjectSharingSettings result) {
        sharingSettingsList.setListData(result.getSharingSettings());
        defaultSharingSettingPanel.setDefaultSharingSetting(result.getDefaultSharingPermission());

    }


    public ProjectSharingSettings getSharingSettingsListData() {
        SharingPermission defaultSharingPermission = defaultSharingSettingPanel.getDefaultSharingSetting();
        List<SharingSetting> sharingSettings = sharingSettingsList.getListData();
        return new ProjectSharingSettings(projectId, defaultSharingPermission, sharingSettings);
    }
}

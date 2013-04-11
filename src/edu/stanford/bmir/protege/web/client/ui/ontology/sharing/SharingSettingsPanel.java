package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.SharingSetting;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserSharingSetting;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeLabel;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.ItemListSuggestBox;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.UserIdSuggestOracle;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class SharingSettingsPanel extends WebProtegeDialogForm {
    
    private ProjectId projectId;
    
    private final SharingSettingsList sharingSettingsList;
    
    private final SharingSettingsDefaultSharingSettingPanel defaultSharingSettingPanel;
    
    public static final String PLACE_HOLDER_TEXT = "Enter names or email addresses (1 per line)";

    public SharingSettingsPanel(final ProjectId projectId) {
        this.projectId = projectId;

        defaultSharingSettingPanel = new SharingSettingsDefaultSharingSettingPanel();
        add(defaultSharingSettingPanel);

        WebProtegeLabel listLabel = new WebProtegeLabel("Who has access");
        add(listLabel);
        listLabel.addStyleName("web-protege-header-label");


        sharingSettingsList = new SharingSettingsList();
        
        refillSharingSettingsList(projectId);

        ScrollPanel scrollPanel = new ScrollPanel(sharingSettingsList);
        scrollPanel.setAlwaysShowScrollBars(false);
        scrollPanel.setHeight("200px");
        add(scrollPanel);


        final TextArea addPeopleTextArea = new TextArea();
        addPeopleTextArea.setVisibleLines(5);
        addPeopleTextArea.setCharacterWidth(50);
        addPeopleTextArea.getElement().setAttribute("placeholder", PLACE_HOLDER_TEXT);

        final UserIdSuggestOracle userIdSuggestOracle = new UserIdSuggestOracle(getUsersInSharingSettingsList()) {
            @Override
            public List<UserId> getItemsMatchingExactly(String itemString) {
                List<UserId> userIds = super.getItemsMatchingExactly(itemString);
                if(userIds.isEmpty() && itemString.contains("@")) {
                    userIds.add(UserId.getUserId(itemString.trim()));
                }
                return userIds;
            }
        };
        final ItemListSuggestBox<UserId> suggestBox = new ItemListSuggestBox<UserId>(userIdSuggestOracle, addPeopleTextArea);


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
        add.addStyleName("web-protege-dialog-button");
        addPeoplePanel.add(add);
        

        add(addPeoplePanel);
    }

    private void handleAdd(ItemListSuggestBox<UserId> suggestBox, SharingSettingsDropDown lb, TextArea addPeopleTextArea) {
        Set<UserId> items = suggestBox.getItems();
        items.removeAll(getUsersInSharingSettingsList());
        List<UserSharingSetting> listDataItems = new ArrayList<UserSharingSetting>(sharingSettingsList.getListData());
        for(UserId item : items) {
            listDataItems.add(new UserSharingSetting(item, lb.getSelectedItem()));
        }
        addPeopleTextArea.setText("");
        sharingSettingsList.setListData(listDataItems);
    }

    private List<UserId> getUsersInSharingSettingsList() {
        List<UserId> result = new ArrayList<UserId>();
        for(UserSharingSetting item : sharingSettingsList.getListData()) {
            result.add(item.getUserId());
        }
        return result;
    }

    private void refillSharingSettingsList(final ProjectId projectId) {
        SharingSettingsServiceAsync sharingSettingsService = SharingSettingsServiceManager.getService();

        sharingSettingsService.getProjectSharingSettings(projectId, new AsyncCallback<ProjectSharingSettings>() {
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            public void onSuccess(ProjectSharingSettings result) {
                updateListData(result);
            }
        });
    }

    private void updateListData(ProjectSharingSettings result) {
        sharingSettingsList.setListData(result.getSharingSettings());
        defaultSharingSettingPanel.setDefaultSharingSetting(result.getDefaultSharingSetting());

    }


    public ProjectSharingSettings getSharingSettingsListData() {
        SharingSetting defaultSharingSetting = defaultSharingSettingPanel.getDefaultSharingSetting();
        List<UserSharingSetting> sharingSettings = sharingSettingsList.getListData();
        return new ProjectSharingSettings(projectId, defaultSharingSetting, sharingSettings);
    }
}

package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeLabel;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.ItemListSuggestBox;
import edu.stanford.bmir.protege.web.client.ui.library.itemarea.UserIdSuggestOracle;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

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
    
    public static final String PLACE_HOLDER_TEXT = "Enter names (1 per line)";

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
                // TODO:  This disables inviting users by email.  Consider whether we really need or want to support this.
//                if(userIds.isEmpty() && itemString.contains("@")) {
//                    userIds.add(UserId.getUserId(itemString.trim()));
//                }
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
        add.addStyleName("button-style");
        addPeoplePanel.add(add);
        

        add(addPeoplePanel);
    }

    private void handleAdd(ItemListSuggestBox<UserId> suggestBox, SharingSettingsDropDown lb, TextArea addPeopleTextArea) {
        Set<UserId> items = suggestBox.getItems();
        items.removeAll(getUsersInSharingSettingsList());
        List<SharingSetting> listDataItems = new ArrayList<SharingSetting>(sharingSettingsList.getListData());
        for(UserId item : items) {
            listDataItems.add(new SharingSetting(item, lb.getSelectedItem()));
        }
        addPeopleTextArea.setText("");
        sharingSettingsList.setListData(listDataItems);
    }

    private List<UserId> getUsersInSharingSettingsList() {
        List<UserId> result = new ArrayList<UserId>();
        for(SharingSetting item : sharingSettingsList.getListData()) {
            result.add(item.getUserId());
        }
        return result;
    }

    private void refillSharingSettingsList(final ProjectId projectId) {
        DispatchServiceManager.get().execute(new GetProjectSharingSettingsAction(projectId), new DispatchServiceCallback<GetProjectSharingSettingsResult>() {
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

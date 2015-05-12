package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.itemlist.ItemListSuggestBox;
import edu.stanford.bmir.protege.web.client.itemlist.ItemListSuggestOracle;
import edu.stanford.bmir.protege.web.client.itemlist.PersonIdItemListSuggestionOracle;
import edu.stanford.bmir.protege.web.client.itemlist.ValueBoxCursorPositionProvider;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.sharing.*;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.HashSet;
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

        final SuggestBox suggestBox = new ItemListSuggestBox<>(new PersonIdItemListSuggestionOracle(addPeopleTextArea, DispatchServiceManager.get()), addPeopleTextArea);


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

    private void handleAdd(SuggestBox suggestBox, SharingSettingsDropDown lb, TextArea addPeopleTextArea) {
        String suggestBoxText = suggestBox.getText();
        String [] names = suggestBoxText.trim().split("\n");
        Set<PersonId> personIds = new HashSet<>();
        for(String name : names) {
            personIds.add(new PersonId(name));
        }
        personIds.removeAll(getUsersInSharingSettingsList());

        List<SharingSetting> listDataItems = new ArrayList<>(sharingSettingsList.getListData());
        for(PersonId personId : personIds) {
            listDataItems.add(new SharingSetting(personId, lb.getSelectedItem()));
        }
        addPeopleTextArea.setText("");
        sharingSettingsList.setListData(listDataItems);
    }

    private List<PersonId> getUsersInSharingSettingsList() {
        List<PersonId> result = new ArrayList<>();
        for(SharingSetting item : sharingSettingsList.getListData()) {
            result.add(item.getPersonId());
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

package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class SharingSettingsList extends FlowPanel {

    public static final String WEB_PROTEGE_TABLE_CELL_STYLE_NAME = "web-protege-table-cell";
    
    public static final String WEB_PROTEGE_SHARING_SETTINGS_LIST = "web-protege-sharing-settings-list";

    private final FlexTable flexTable;

    private final List<SharingSetting> displayedItems = new ArrayList<SharingSetting>();

    private final LoggedInUserProvider loggedInUserProvider;

    public SharingSettingsList(LoggedInUserProvider loggedInUserProvider) {
        this.loggedInUserProvider = loggedInUserProvider;
        addStyleName(WEB_PROTEGE_SHARING_SETTINGS_LIST);
        flexTable = new FlexTable();
        flexTable.setWidth("100%");
        add(flexTable);
        refill();
    }

    public void setListData(List<SharingSetting> listData) {
        displayedItems.clear();
        displayedItems.addAll(listData);
        refill();
    }

    public List<SharingSetting> getListData() {
        return new ArrayList<SharingSetting>(displayedItems);
    }

    protected void refill() {
        flexTable.removeAllRows();
        for (SharingSetting listItem : displayedItems) {
            addData(listItem);
        }
    }

    private void addData(final SharingSetting listItem) {
        final int rowCount = flexTable.getRowCount();
        String personId = listItem.getPersonId().getId();
        UserId userId = loggedInUserProvider.getCurrentUserId();
        if (personId.equals(userId.getUserName())) {
            personId += " (you)";
        }
        flexTable.setText(rowCount, 0, personId);


        final SharingSettingsDropDown lb = new SharingSettingsDropDown();
        lb.setSelectedItem(listItem.getSharingPermission());
        
        lb.addValueChangeHandler(new ValueChangeHandler<SharingPermission>() {
            public void onValueChange(ValueChangeEvent<SharingPermission> valueChangeEvent) {
                SharingPermission value = valueChangeEvent.getValue();
                SharingSetting updatedType = new SharingSetting(listItem.getPersonId(), value);
                displayedItems.set(rowCount, updatedType);
            }
        });
        
        flexTable.setWidget(rowCount, 1, lb);
        flexTable.getRowFormatter().addStyleName(rowCount, "web-protege-table-row");

        if (!userId.getUserName().equals(personId)) {
            DeleteButton deleteButton = new DeleteButton();
            deleteButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    displayedItems.remove(rowCount);
                    refill();
                }
            });
            flexTable.setWidget(rowCount, 2, deleteButton);
        }

        HTMLTable.CellFormatter cellFormatter = flexTable.getCellFormatter();
        cellFormatter.addStyleName(rowCount, 0, WEB_PROTEGE_TABLE_CELL_STYLE_NAME);
        cellFormatter.addStyleName(rowCount, 1, WEB_PROTEGE_TABLE_CELL_STYLE_NAME);
        cellFormatter.addStyleName(rowCount, 2, WEB_PROTEGE_TABLE_CELL_STYLE_NAME);
    }



}

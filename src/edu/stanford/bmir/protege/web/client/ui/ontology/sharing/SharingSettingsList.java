package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.data.SharingSetting;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserSharingSetting;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;

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

    private List<UserSharingSetting> displayedItems = new ArrayList<UserSharingSetting>();

    public SharingSettingsList() {
        addStyleName(WEB_PROTEGE_SHARING_SETTINGS_LIST);
        flexTable = new FlexTable();
        flexTable.setWidth("100%");
        add(flexTable);
        refill();
    }

    public void setListData(List<UserSharingSetting> listData) {
        displayedItems.clear();
        displayedItems.addAll(listData);
        refill();
    }

    public List<UserSharingSetting> getListData() {
        return new ArrayList<UserSharingSetting>(displayedItems);
    }

    protected void refill() {
        flexTable.removeAllRows();
        for (UserSharingSetting listItem : displayedItems) {
            addData(listItem);
        }
    }

    private void addData(final UserSharingSetting listItem) {
        final int rowCount = flexTable.getRowCount();
        String userName = listItem.getUserId().getUserName();
        UserId userId = Application.get().getUserId();
        if (userName.equals(userId.getUserName())) {
            userName += " (you)";
        }
        flexTable.setText(rowCount, 0, userName);


        final SharingSettingsDropDown lb = new SharingSettingsDropDown();
        lb.setSelectedItem(listItem.getSharingSetting());
        
        lb.addValueChangeHandler(new ValueChangeHandler<SharingSetting>() {
            public void onValueChange(ValueChangeEvent<SharingSetting> valueChangeEvent) {
                SharingSetting value = valueChangeEvent.getValue();
                UserSharingSetting updatedType = new UserSharingSetting(listItem.getUserId(), value);
                displayedItems.set(rowCount, updatedType);
            }
        });
        
        flexTable.setWidget(rowCount, 1, lb);
        flexTable.getRowFormatter().addStyleName(rowCount, "web-protege-table-row");

        if (!userId.getUserName().equals(userName)) {
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

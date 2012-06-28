package edu.stanford.bmir.protege.web.client.ui.util;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class PaginationUtil {

    public static final String PROJECT_NAME_KEY = "projectName";
    public static final String ENTITY_NAME_KEY = "entityName";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";
    public static final String USER_NAME_KEY = "userName";
    public static final String TOP_LEVEL_KEY = "topLevel";
    public static final String SEARCH_TEXT_KEY = "searchText";

    public static PagingToolbar getNewPagingToolbar(Store st, int pageSize) {
        final PagingToolbar pagingToolbar = new PagingToolbar(st);
        pagingToolbar.setPageSize(pageSize);
        pagingToolbar.setDisplayInfo(true);
        pagingToolbar.setDisplayMsg("Displaying records {0} - {1} of {2}");
        pagingToolbar.setEmptyMsg("No records to display");

        TextField pageSizeField = new TextField(Integer.toString(pageSize), "pageSize");
        pageSizeField.setWidth(40);
        pageSizeField.setSelectOnFocus(true);
        pageSizeField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    try {
                        int pageSize = Integer.parseInt(field.getValueAsString());
                        if(pageSize > 0) {
                            pagingToolbar.setPageSize(pageSize);
                        }
                    } catch (NumberFormatException nfe) {
                        GWT.log("Invalid number in Pagination", nfe);
                    }
                }
            }
        });

        pagingToolbar.doOnRender(new Function() {
            public void execute() {
                try {
                    pagingToolbar.getRefreshButton().hide();
                } catch(IllegalStateException ise) {
                    GWT.log("PagingToolbar.getRefreshButton() called before doOnRender()", ise);
                }
            }
        });

        ToolTip toolTip = new ToolTip("Enter page size");
        toolTip.applyTo(pageSizeField);

        pagingToolbar.addField(pageSizeField);

        return pagingToolbar;
    }
}

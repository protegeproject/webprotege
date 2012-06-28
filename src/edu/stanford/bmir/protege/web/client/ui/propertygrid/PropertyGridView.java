package edu.stanford.bmir.protege.web.client.ui.propertygrid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.Panel;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequestEntityMatchType;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualObject;
import edu.stanford.bmir.protege.web.client.rpc.data.tuple.TripleTuple;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class PropertyGridView extends Panel {

    public static final int SUGGEST_LIMIT = 50;

    public static final String SUGGEST_BOX_WIDTH = "96%";

    public static final String PROPERTY_NAME_PLACEHOLDER_TEXT = "Enter property name";

    public static final String CLASS_NAME_PLACE_HOLDER_TEXT = "Enter class name";

    private FlowPanel mainPanel = new FlowPanel();

    private FlexTable flexTable = new FlexTable();

    private Entity subject;
    
    private Project project;

    private final EntitySuggestOracle objectPropertySuggestOracle;

    private final EntitySuggestOracle classSuggestOracle;

    private boolean dirty = false;

    public PropertyGridView(Project project) {
        addStyleName("web-protege-laf");
        add(mainPanel);
        mainPanel.add(flexTable);
        flexTable.setWidth("100%");
        this.project = project;
        objectPropertySuggestOracle = new EntitySuggestOracle(getProjectId(), SUGGEST_LIMIT, EntityLookupRequestEntityMatchType.MATCH_OBJECT_PROPERTIES);
        classSuggestOracle = new EntitySuggestOracle(getProjectId(), SUGGEST_LIMIT, EntityLookupRequestEntityMatchType.MATCH_CLASSES);
    }

    public boolean isDirty() {
        return dirty;
    }

    public ProjectId getProjectId() {
        return new ProjectId(project.getProjectName());
    }

    public void setSubject(Entity subject) {
        this.subject = subject;
        refill();
    }

    public void clearSubject() {
        subject = null;
        refill();
    }

    public void refill() {
        if (subject != null) {
            PropertyGridServiceAsync propertyGridService = GWT.create(PropertyGridService.class);
            propertyGridService.getPropertyGrid(getProjectId(), subject, new AsyncCallback<PropertyGrid>() {
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                public void onSuccess(PropertyGrid result) {
                    fillGrid(result);
                }
            });
        }
        else {
            flexTable.removeAllRows();
            ensureBlankRow();
        }

    }

    private void fillGrid(PropertyGrid propertyGrid) {
        flexTable.removeAllRows();
        for (TripleTuple<?, ?, ?> triple : propertyGrid.getGridEntries()) {
            VisualObject<?> property = triple.getVisualProperty();
            VisualObject<?> visualObject = triple.getVisualObject();
            addRow(property, visualObject);
        }
        ensureBlankRow();
    }

    private void addRow(VisualObject<?> property, VisualObject<?> visualObject) {
        String propertyBrowserText = property.getBrowserText();
        String fillerBrowserText = visualObject.getBrowserText();
        addRow(propertyBrowserText, fillerBrowserText);
    }

    private void addBlankRow() {
        addRow("", "");
    }
    
    private void ensureBlankRow() {
        if(!isLastRowEmpty()) {
            addBlankRow();
        }
    }
    
    private String getTextAt(int row, int col) {
        Widget widget = flexTable.getWidget(row, col);
        if (widget instanceof SuggestBox) {
            SuggestBox suggestBox = (SuggestBox) widget;
            return suggestBox.getText().trim();
        }
        else {
            return "";
        }
    }
    
    private boolean isEmpty(int row, int col) {
        return getTextAt(row, col).isEmpty();
    }
    
    private boolean isEmpty(int row) {
        return isEmpty(row, 0) && isEmpty(row, 1);
    }
    
    private boolean isLastRowEmpty() {
        int rowCount = flexTable.getRowCount();
        return rowCount != 0 && isEmpty(rowCount - 1);
    }
    

    private void addRow(String propertyBrowserText, String fillerBrowserText) {
        final int row = flexTable.getRowCount();
        SuggestBox propertySuggestBox = new SuggestBox(objectPropertySuggestOracle);
        propertySuggestBox.setText(propertyBrowserText);
        propertySuggestBox.setWidth(SUGGEST_BOX_WIDTH);
        propertySuggestBox.getElement().setAttribute("placeholder", PROPERTY_NAME_PLACEHOLDER_TEXT);
        propertySuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                dirty = true;
            }
        });
        flexTable.setWidget(row, 0, propertySuggestBox);

        final SuggestBox fillerSuggestBox = new SuggestBox(classSuggestOracle);
        fillerSuggestBox.setText(fillerBrowserText);
        fillerSuggestBox.setWidth(SUGGEST_BOX_WIDTH);
        fillerSuggestBox.getElement().setAttribute("placeholder", CLASS_NAME_PLACE_HOLDER_TEXT);
        fillerSuggestBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !fillerSuggestBox.isSuggestionListShowing()) {
                    ensureBlankRow();
                }
            }
        });
        fillerSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                dirty = true;
            }
        });
        flexTable.setWidget(row, 1, fillerSuggestBox);
    }

}

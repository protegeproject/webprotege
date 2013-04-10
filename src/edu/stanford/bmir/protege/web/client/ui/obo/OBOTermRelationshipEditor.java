package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBORelationship;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.semanticweb.owlapi.model.EntityType;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermRelationshipEditor implements OBOTermEditor {

    public static final int PROPERTY_COLUMN = 0;
    
    public static final int FILLER_COLUMN = 1;

//    private static final String SUGGEST_BOX_WIDTH = "100%";
    
    private static final String PROPERTY_NAME_PLACEHOLDER_TEXT = "Enter property name";
    
    private static final String CLASS_NAME_PLACE_HOLDER_TEXT = "Enter class name";

    public static final String WEB_PROTEGE_ERROR_BACKGROUND_STYLE = "web-protege-error-background";

    private FlexTable table = new FlexTable();
    
    private ProjectId projectId;

    private final EntitySuggestOracle objectPropertySuggestOracle;

    private final EntitySuggestOracle fillerSuggestOracle;
    
    private Map<String, OWLObjectPropertyData> objectPropertyBrowserText = new HashMap<String, OWLObjectPropertyData>();
    
    private Map<String, OWLClassData> clsBrowserText = new HashMap<String, OWLClassData>();

    private boolean dirty = false;
    
    
    public OBOTermRelationshipEditor(ProjectId projectId) {
        this.projectId = projectId;
        objectPropertySuggestOracle = new EntitySuggestOracle(this.projectId, 20, EntityType.OBJECT_PROPERTY);
        fillerSuggestOracle = new EntitySuggestOracle(this.projectId, 20, EntityType.CLASS);
        table.setBorderWidth(0);
        table.setCellPadding(0);
        table.setCellSpacing(0);
    }

    public void setValue(OBOTermRelationships relationships) {
        table.removeAllRows();
        for(OBORelationship relationship : relationships.getRelationships()) {
            addRelationship(relationship);
        }
        ensureBlankRow();
        dirty = false;
    }

    public void clearValue() {
        setValue(new OBOTermRelationships(Collections.<OBORelationship>emptySet()));
        dirty = false;
    }
    
    public OBOTermRelationships getValue() {
        List<OBORelationship> relationships = new ArrayList<OBORelationship>();
        for(int i = 0; i < table.getRowCount(); i++) {
            String propertyBrowserText = getTextAt(i, 0);
            OWLObjectPropertyData prop = objectPropertyBrowserText.get(propertyBrowserText);
            String fillerBrowserText = getTextAt(i, 1);
            OWLClassData cls = clsBrowserText.get(fillerBrowserText);
            if (prop != null && cls != null) {
                relationships.add(new OBORelationship(prop, cls));
            }
        }
        return new OBOTermRelationships(new HashSet<OBORelationship>(relationships));
    }
    
    
    private void addRelationship(OBORelationship relationship) {
        addRow(relationship.getRelation(), relationship.getValue());
    }


    public int getEditorCount() {
        return 1;
    }

    public String getLabel(int index) {
        return "Relationships";
    }

    public Widget getEditorWidget(int index) {
        return table;
    }

    public boolean hasXRefs() {
        return false;
    }

    public XRefListEditor getXRefListEditor() {
        return null;
    }

    public boolean isDirty() {
        return dirty;
    }

    private void addRow(OWLObjectPropertyData property, OWLClassData cls) {
        objectPropertyBrowserText.put(property.getBrowserText(), property);
        clsBrowserText.put(cls.getBrowserText(), cls);
        String propertyBrowserText = property.getBrowserText();
        String fillerBrowserText = cls.getBrowserText();
        addRow(propertyBrowserText, fillerBrowserText);
    }

    private void addBlankRow() {
        addRow("", "");
    }

    private void addRow(String propertyText, String fillerBrowserText) {
        int rowCount = table.getRowCount();
        final SuggestBox propertyField = new SuggestBox(objectPropertySuggestOracle);
        propertyField.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                dirty = true;
                updatePropertyBrowserTextHighlighting(propertyField);
            }
        });
        table.setWidget(rowCount, PROPERTY_COLUMN, propertyField);
        propertyField.setText(propertyText);
        propertyField.addStyleName("web-protege-form-layout-editor-input");
        propertyField.getElement().setAttribute("placeholder", PROPERTY_NAME_PLACEHOLDER_TEXT);
        propertyField.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                EntitySuggestion suggestion = (EntitySuggestion) suggestionSelectionEvent.getSelectedItem();
                objectPropertyBrowserText.put(suggestion.getReplacementString(), (OWLObjectPropertyData) suggestion.getEntity());
                dirty = true;
            }
        });

        final SuggestBox fillerField = new SuggestBox(fillerSuggestOracle);
        fillerField.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                dirty = true;
                checkFillerText(fillerField);
            }
        });
        fillerField.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                EntitySuggestion suggestion = (EntitySuggestion) suggestionSelectionEvent.getSelectedItem();
                clsBrowserText.put(suggestion.getReplacementString(), (OWLClassData) suggestion.getEntity());
                dirty = true;
            }
        });

        table.setWidget(rowCount, FILLER_COLUMN, fillerField);
        fillerField.setText(fillerBrowserText);
        fillerField.addStyleName("web-protege-form-layout-editor-input");
        fillerField.getElement().setAttribute("placeholder", CLASS_NAME_PLACE_HOLDER_TEXT);
        fillerField.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !fillerField.isSuggestionListShowing()) {
                    ensureBlankRow();
                }
            }
        });
    }

    /**
     * Checks the text in a property suggest box to see if the text corresponds to the browser text of an entity in
     * the project root ontology imports closure.  If not, the suggest box is highlighted using the web-protege-error-background
     * style and its title is set to display a tooltip.
     * @param propertySuggestBox The property suggest box to validate.
     */
    private void updatePropertyBrowserTextHighlighting(SuggestBox propertySuggestBox) {
        String propertyBrowserText = propertySuggestBox.getText().trim();
        if(!propertyBrowserText.isEmpty() && objectPropertyBrowserText.get(propertyBrowserText) == null) {
            propertySuggestBox.addStyleName(WEB_PROTEGE_ERROR_BACKGROUND_STYLE);
            propertySuggestBox.setTitle("Relation not recognized");
        }
        else {
            propertySuggestBox.removeStyleName(WEB_PROTEGE_ERROR_BACKGROUND_STYLE);
            propertySuggestBox.setTitle("");
        }
    }

    /**
     * Checks the text in a filler suggest box to see if the text corresponds to the browser text of an entity in
     * the project root ontology imports closure.  If not, the suggest box is highlighted using the web-protege-error-background
     * style and its title is set to display a tooltip.
     * @param fillerSuggestBox The filler suggest box to validate.
     */
    private void checkFillerText(SuggestBox fillerSuggestBox) {
        String fillerBrowserText = fillerSuggestBox.getText().trim();
        if(!clsBrowserText.isEmpty() && clsBrowserText.get(fillerBrowserText) == null) {
            fillerSuggestBox.addStyleName(WEB_PROTEGE_ERROR_BACKGROUND_STYLE);
            fillerSuggestBox.setTitle("Class not recognized");
        }
        else {
            fillerSuggestBox.removeStyleName(WEB_PROTEGE_ERROR_BACKGROUND_STYLE);
            fillerSuggestBox.setTitle("");
        }
    }
    
    private void ensureBlankRow() {
        if(!isLastRowEmpty()) {
            addBlankRow();
        }
    }

    private String getTextAt(int row, int col) {
        Widget widget = table.getWidget(row, col);
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
        int rowCount = table.getRowCount();
        return rowCount != 0 && isEmpty(rowCount - 1);
    }

}

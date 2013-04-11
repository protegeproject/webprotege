package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermCrossProductEditor implements OBOTermEditor {

    private SuggestBox genusSuggestBox;
    
    private OBOTermRelationshipEditor relationshipEditor;

    private boolean dirty;

    private Map<String, OWLClassData> browserTextToClassMap = new HashMap<String, OWLClassData>();
    
    public OBOTermCrossProductEditor(ProjectId projectId) {
        genusSuggestBox = new SuggestBox(new EntitySuggestOracle(projectId, 20, EntityType.CLASS));
        relationshipEditor = new OBOTermRelationshipEditor(projectId);
        genusSuggestBox.getElement().setAttribute("placeholder", "Type in genus name (class name)");
        genusSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                EntitySuggestion suggestion = (EntitySuggestion) suggestionSelectionEvent.getSelectedItem();
                browserTextToClassMap.put(suggestion.getReplacementString(), (OWLClassData) suggestion.getEntity());
            }
        });
        genusSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                dirty = true;
            }
        });
    }

    public int getEditorCount() {
        return 1 + relationshipEditor.getEditorCount();
    }

    public void setValue(OBOTermCrossProduct crossProduct) {
        OWLEntityData genus = crossProduct.getGenus();
        if (genus != null) {
            genusSuggestBox.setValue(genus.getBrowserText());
        }
        else {
            genusSuggestBox.setValue("");
        }
        relationshipEditor.setValue(crossProduct.getRelationships());
        dirty = false;
    }

    public OBOTermCrossProduct getValue() {
        String genusValue = genusSuggestBox.getValue().trim();
        OWLClassData genus = null;
        if(!genusValue.isEmpty()) {
            genus = browserTextToClassMap.get(genusValue);
        }
        return new OBOTermCrossProduct(genus, relationshipEditor.getValue());
    }

    public void clearValue() {
        genusSuggestBox.setText("");
        relationshipEditor.clearValue();
        dirty = false;
    }

    public boolean isDirty() {
        return dirty || relationshipEditor.isDirty();
    }

    public String getLabel(int index) {
        if(index == 0) {
            return "Genus";
        }
        else {
            return "Discriminating relationships";
        }
    }

    public Widget getEditorWidget(int index) {
        if(index == 0) {
            return genusSuggestBox;
        }
        else {
            return relationshipEditor.getEditorWidget(index - 1);
        }
    }

    public boolean hasXRefs() {
        return false;
    }

    public XRefListEditor getXRefListEditor() {
        return null;
    }
}

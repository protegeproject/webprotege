package edu.stanford.bmir.protege.web.client.library.suggest;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntitySuggestion implements SuggestOracle.Suggestion {

    private OWLEntityData entity;
    
    private String displayText;

    /**
     * Creates an entity suggestion.
     * @param entity The entity being suggested.
     * @param displayText The text (can be HTML) that appears in the suggestion popup.  This is not necessarily the
     * same as browser text.
     */
    public EntitySuggestion(OWLEntityData entity, String displayText) {
        this.entity = entity;
        this.displayText = displayText;
    }

    public OWLEntityData getEntity() {
        return entity;
    }

    public String getDisplayString() {
        return displayText;
    }

    public String getReplacementString() {
        return entity.getBrowserText();
    }
}

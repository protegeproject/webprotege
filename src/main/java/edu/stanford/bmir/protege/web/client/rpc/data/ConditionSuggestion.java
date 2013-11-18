package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.List;

public class ConditionSuggestion  implements Serializable {

    private List<EntityData> suggestions;
    private String message;
    private boolean isValid;

    public ConditionSuggestion() {

    }

    public List<EntityData> getSuggestions() {
        return suggestions;
    }
    public void setSuggestions(List<EntityData> suggestions) {
        this.suggestions = suggestions;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }



}

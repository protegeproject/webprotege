package edu.stanford.bmir.protege.web.shared.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
@Deprecated
public enum NoteStatus {

    OPEN("unresolved"),

    RESOLVED("resolved");

    private String displayText;

    /**
     * For serialization only
     */
    NoteStatus() {
    }

    NoteStatus(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}

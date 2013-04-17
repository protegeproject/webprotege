package edu.stanford.bmir.protege.web.shared.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public enum NoteStatus {

    OPEN("open"),

    RESOLVED("resolved");

    private String displayText;

    /**
     * For serialization only
     */
    private NoteStatus() {
    }

    private NoteStatus(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}

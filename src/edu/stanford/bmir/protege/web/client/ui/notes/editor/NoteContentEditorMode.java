package edu.stanford.bmir.protege.web.client.ui.notes.editor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public enum NoteContentEditorMode {

    NEW_TOPIC("Post new topic"),

    REPLY("Reply to note");

    private String modeTitle;

    private NoteContentEditorMode(String modeTitle) {
        this.modeTitle = modeTitle;
    }

    public String getModeTitle() {
        return modeTitle;
    }
}


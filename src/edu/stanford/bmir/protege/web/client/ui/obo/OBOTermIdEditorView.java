package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermIdEditorView implements OBOTermEditor {

    private OBOTermIdEditor editor = new OBOTermIdEditor();

    public int getEditorCount() {
        return editor.getEditorCount();
    }

    public String getLabel(int index) {
        return editor.getLabel(index);
    }

    public Widget getEditorWidget(int index) {
        return editor.getEditorWidget(index);
    }

    public String getHeight(int index) {
        return "1em";
    }

    public boolean hasXRefs() {
        return false;
    }

    public XRefListEditor getXRefListEditor() {
        return null;
    }

    public boolean isDirty() {
        return editor.isDirty();
    }
}

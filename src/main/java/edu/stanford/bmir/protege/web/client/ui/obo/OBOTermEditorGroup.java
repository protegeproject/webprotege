package edu.stanford.bmir.protege.web.client.ui.obo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermEditorGroup {

    private List<OBOTermEditor> termEditors = new ArrayList<OBOTermEditor>();

    public OBOTermEditorGroup(List<OBOTermEditor> termEditors) {
        this.termEditors = termEditors;
    }

    public OBOTermEditorGroup() {
    }

    public List<OBOTermEditor> getTermEditors() {
        return termEditors;
    }

    protected void clearEditors() {
        termEditors.clear();
    }

    protected void addEditor(OBOTermEditor editor) {
        termEditors.add(editor);
    }

}

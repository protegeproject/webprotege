package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermSynonym;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermSynonymListEditor extends OBOTermEditorGroup {

    private List<OBOTermSynonymEditor> editors = new ArrayList<OBOTermSynonymEditor>();

    public OBOTermSynonymListEditor() {
    }
    
    public void setValues(List<OBOTermSynonym> synonyms) {
        editors.clear();
        clearEditors();
        for(OBOTermSynonym synonym : synonyms) {
            OBOTermSynonymEditor editor = new OBOTermSynonymEditor();
            editor.setValue(synonym);
            editors.add(editor);
            addEditor(editor);
        }
//        if(synonyms.isEmpty()) {
            addBlankEditor();
//        }
    }

    private void addBlankEditor() {
        OBOTermSynonymEditor editor = new OBOTermSynonymEditor();
        editors.add(editor);
        addEditor(editor);
    }

    public List<OBOTermSynonym> getValues() {
        List<OBOTermSynonym> result = new ArrayList<OBOTermSynonym>();
        for(OBOTermSynonymEditor editor : editors) {
            OBOTermSynonym syn = editor.getValue();
            if (!syn.isEmpty()) {
                result.add(syn);
            }
        }
        return result;
    }
    
    public boolean isDirty() {

        for(OBOTermSynonymEditor editor : editors) {
            if(editor.isDirty()) {
                return true;
            }
        }
        return false;
    }
}

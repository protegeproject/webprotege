package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOTermDefinition;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermDefinitionEditor implements OBOTermEditor  {

    private TextArea definitionField = new TextArea();

    private final XRefListEditor xrefListEditor;

    private boolean dirty = false;

    public OBOTermDefinitionEditor() {
        definitionField.setVisibleLines(5);
        definitionField.setCharacterWidth(70);
        definitionField.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                dirty = true;
            }
        });
        xrefListEditor = new XRefListEditor();
    }

    public void setValue(OBOTermDefinition definition) {
        definitionField.setValue(definition.getDefinition());
        xrefListEditor.setValues(definition.getXRefs());
        dirty = false;
    }

    public boolean isDirty() {
        return dirty || xrefListEditor.isDirty();
    }

    public OBOTermDefinition getValue() {
        return new OBOTermDefinition(xrefListEditor.getValues(), getDefinition());
    }

    private String getDefinition() {
        return definitionField.getValue().trim();
    }

    public void clearValue() {
        definitionField.setValue("");
        xrefListEditor.clearValues();
    }


    public int getEditorCount() {
        return 1;
    }

    public String getLabel(int index) {
        return "Definition";
    }

    public Widget getEditorWidget(int index) {
        return definitionField;
    }

    public boolean hasXRefs() {
        return true;
    }

    public XRefListEditor getXRefListEditor() {
        return xrefListEditor;
    }
}

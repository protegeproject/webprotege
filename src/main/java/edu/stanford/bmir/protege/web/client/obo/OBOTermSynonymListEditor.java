package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonym;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class OBOTermSynonymListEditor extends ValueListEditorImpl<OBOTermSynonym> {

    public OBOTermSynonymListEditor() {
        super(new ValueEditorFactory<OBOTermSynonym>() {
            @Override
            public ValueEditor<OBOTermSynonym> createEditor() {
                return new OBOTermSynonymEditorImpl();
            }
        });
    }
}

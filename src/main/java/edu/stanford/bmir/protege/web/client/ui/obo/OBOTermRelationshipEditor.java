package edu.stanford.bmir.protege.web.client.ui.obo;

import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermRelationshipEditor extends ValueListEditorImpl<OBORelationship> {

    public OBOTermRelationshipEditor() {
        super(new ValueEditorFactory<OBORelationship>() {
            @Override
            public ValueEditor<OBORelationship> createEditor() {
                return new OBORelationshipEditorImpl();
            }
        });
    }

}

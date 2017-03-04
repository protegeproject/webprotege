package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermRelationshipEditor extends ValueListEditorImpl<OBORelationship> {

    @Inject
    public OBOTermRelationshipEditor(final Provider<PrimitiveDataEditorImpl> primitiveDataEditorProvider) {
        super(new ValueEditorFactory<OBORelationship>() {
            @Override
            public ValueEditor<OBORelationship> createEditor() {
                return new OBORelationshipEditorImpl(
                        primitiveDataEditorProvider.get(),
                        primitiveDataEditorProvider.get());
            }
        });
    }

}

package edu.stanford.bmir.protege.web.client.obo;

import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.obo.OBORelationship;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OBOTermRelationshipEditor extends ValueListFlexEditorImpl<OBORelationship> {

    @Inject
    public OBOTermRelationshipEditor(final Provider<PrimitiveDataEditorImpl> primitiveDataEditorProvider) {
        super(() -> new OBORelationshipEditorImpl(
                primitiveDataEditorProvider.get(),
                primitiveDataEditorProvider.get()));
    }

}

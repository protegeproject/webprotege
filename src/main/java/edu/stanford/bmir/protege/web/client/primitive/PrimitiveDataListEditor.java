package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/12/2012
 */
public class PrimitiveDataListEditor extends ValueListEditorImpl<OWLPrimitiveData> implements HasEnabled  {

    public PrimitiveDataListEditor(final PrimitiveType allowedType) {
        super(new ValueEditorFactory<OWLPrimitiveData>() {
            @Override
            public ValueEditor<OWLPrimitiveData> createEditor() {
                PrimitiveDataEditorImpl editor = PrimitiveDataEditorGinjector.INSTANCE.getEditor();
                editor.setAllowedType(allowedType, true);
                editor.setFreshEntitiesSuggestStrategy(new SimpleFreshEntitySuggestStrategy());
                return editor;
            }
        });
    }
}

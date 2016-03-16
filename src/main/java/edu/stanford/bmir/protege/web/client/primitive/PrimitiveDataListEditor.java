package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Arrays;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/12/2012
 */
public class PrimitiveDataListEditor extends ValueListEditorImpl<OWLPrimitiveData> implements HasEnabled  {

    public PrimitiveDataListEditor(final ProjectId projectId, final PrimitiveType ... allowedTypes) {
        super(new ValueEditorFactory<OWLPrimitiveData>() {
            @Override
            public ValueEditor<OWLPrimitiveData> createEditor() {
                PrimitiveDataEditorImpl editor = WebProtegeClientInjector.getPrimitiveDataEditor(projectId);
                editor.setAllowedTypes(Arrays.asList(allowedTypes));
                editor.setFreshEntitiesSuggestStrategy(new SimpleFreshEntitySuggestStrategy());
                return editor;
            }
        });
    }
}

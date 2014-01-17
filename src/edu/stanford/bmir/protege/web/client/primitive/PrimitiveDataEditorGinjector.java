package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
@GinModules(PrimitiveDataEditorClientModule.class)
public interface PrimitiveDataEditorGinjector extends Ginjector {

    public static final PrimitiveDataEditorGinjector INSTANCE = GWT.create(PrimitiveDataEditorGinjector.class);

    PrimitiveDataEditor getEditor();
}

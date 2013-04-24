package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorManager<C extends EditorCtx, V extends Serializable> {

    String getDescription(C editorContext);

    EditorView<V> getView(C editorContext);

    GetObjectAction<V> createGetObjectAction(C editorContext);

    UpdateObjectAction<V> createUpdateObjectAction(V pristineObject, V editedObject, C editorContext);
}

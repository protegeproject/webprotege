package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.shared.HasObject;
import edu.stanford.bmir.protege.web.shared.dispatch.*;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorManager<C extends EditorCtx, V, A extends Action<R>, R extends Result> {

    String getDescription(C editorContext);

    EditorView<V> getView(C editorContext);

    UpdateObjectAction<V> createUpdateObjectAction(V pristineObject, V editedObject, C editorContext);

    A createAction(C editorContext);

    V extractObject(R result);
}

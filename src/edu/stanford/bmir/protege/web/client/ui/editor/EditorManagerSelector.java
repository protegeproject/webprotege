package edu.stanford.bmir.protege.web.client.ui.editor;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorManagerSelector {

    boolean canEditContext(EditorCtx editorCtx);

    <C extends EditorCtx, O extends Serializable> EditorManager<C, O> getEditorManager(EditorCtx editorContext);
}

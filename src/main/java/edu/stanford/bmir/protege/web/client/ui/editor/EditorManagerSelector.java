package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorManagerSelector {

    boolean canEditContext(EditorCtx editorCtx);

    <C extends EditorCtx, O, A extends Action<R>, R extends Result> EditorManager<C, O, A, R> getEditorManager(EditorCtx editorContext);
}

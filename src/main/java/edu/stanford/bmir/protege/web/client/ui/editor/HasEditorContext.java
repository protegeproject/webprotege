package edu.stanford.bmir.protege.web.client.ui.editor;

import edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor.EditorContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/01/2013
 */
public interface HasEditorContext<T> {

    void setEditorContext(EditorContext<T> editorContext);

    EditorContext<T> getEditorContext();
}

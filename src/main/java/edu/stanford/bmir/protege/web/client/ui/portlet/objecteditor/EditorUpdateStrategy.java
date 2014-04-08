package edu.stanford.bmir.protege.web.client.ui.portlet.objecteditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 */
public interface EditorUpdateStrategy<T> {

    boolean shouldUpdateEditor(EditorContext<T> editorContext);

    String getEditorTitle(EditorContext<T> editorContext);
}

package edu.stanford.bmir.protege.web.client.editor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/06/2013
 */
public interface ValueEditorFactory<O> {

    ValueEditor<O> createEditor();
}

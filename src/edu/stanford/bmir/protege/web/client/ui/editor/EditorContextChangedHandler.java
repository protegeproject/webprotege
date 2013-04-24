package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public interface EditorContextChangedHandler extends EventHandler {

    void handleEditorContextChanged(EditorContextChangedEvent editorContextChangedEvent);
}

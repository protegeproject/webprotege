package edu.stanford.bmir.protege.web.client.ui.library.richtext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface RichTextToolbarHandler {

    void handleToggleFontWeight();

    void handleToggleFontStyle();

    void handleToggleTextDecoration();

    void handleInsertUnorderedList();

    void handleInsertOrderedList();

    void handleCreateLink();
}

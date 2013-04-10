package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface DiscussionThreadView {

    void removeAllNotes();

    void addNote(Widget widget, int depth);


    Widget getWidget();
}

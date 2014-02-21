package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface DiscussionThreadView {


    void setPostNewTopicHandler(PostNewTopicHandler handler);

    void setPostNewTopicEnabled(boolean enabled);

    void removeAllNotes();

    void addNote(NoteContainerPresenter notePresenter, int depth);


    Widget getWidget();
}

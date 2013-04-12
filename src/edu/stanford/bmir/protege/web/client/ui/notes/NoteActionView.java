package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface NoteActionView extends IsWidget {

    void setReplyToNoteHandler(ReplyToNoteHandler handler);

    void setDeleteNoteHandler(DeleteNoteHandler handler);

    void setCanDelete(boolean canDelete);

    Widget getWidget();


}

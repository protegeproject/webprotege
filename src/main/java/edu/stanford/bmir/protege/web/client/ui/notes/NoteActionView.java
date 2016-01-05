package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface NoteActionView extends IsWidget {

    void setNoteId(NoteId noteId);

    void setReplyToNoteHandler(ReplyToNoteHandler handler);

    void setCanReply(boolean canReply);

    void setDeleteNoteHandler(DeleteNoteHandler handler);

    void setCanDelete(boolean canDelete);

    Widget getWidget();


}

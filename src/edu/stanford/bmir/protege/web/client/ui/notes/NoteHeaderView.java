package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.notes.NoteStatus;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public interface NoteHeaderView {

    void setSubject(Optional<String> subject);

    void setAuthor(UserId userId);

    void setResolveOptionVisible(boolean b);

    void setStatus(Optional<NoteStatus> noteStatus);

    void setReolveOptionText(String text);

    void setResolveNoteHandler(ResolveNoteHandler handler);

    Widget getWidget();
}

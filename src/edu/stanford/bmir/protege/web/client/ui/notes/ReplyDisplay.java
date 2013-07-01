package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasEditingCancelledHandlers;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasEditingFinishedHandlers;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public interface ReplyDisplay extends ValueEditor<NoteContent>, HasVisibility, HasEditingFinishedHandlers<NoteContent>, HasEditingCancelledHandlers, HasHandlers {

    String getBody();

    Widget getWidget();
}

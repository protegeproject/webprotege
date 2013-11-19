package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public interface NoteView extends IsWidget {

    void setAuthor(String authorName);

    void setTimestamp(long timestamp);

    void setBody(SafeHtml safeHtml);

    Widget getWidget();
}

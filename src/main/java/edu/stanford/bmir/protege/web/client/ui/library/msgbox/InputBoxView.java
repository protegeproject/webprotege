package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public interface InputBoxView extends HasInitialFocusable {

    void setMessage(String msg);

    /**
     * Gets the value entered by the user.
     * @return The string that was entered by the user. Not {@code null}.
     */
    String getInputValue();

    Widget getWidget();
}

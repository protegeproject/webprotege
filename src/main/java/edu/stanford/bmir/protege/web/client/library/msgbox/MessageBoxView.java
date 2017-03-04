package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public interface MessageBoxView extends IsWidget {

    void setMainMessage(String title);

    void setSubMessage(String message);

    void setMessageStyle(MessageStyle messageStyle);
}

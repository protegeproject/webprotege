package edu.stanford.bmir.protege.web.client.progress;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */
public interface ProgressBarView extends IsWidget {

    /**
     * Sets the title for the progress view.
     * @param title The title.  Not {@code null}.
     * @throws  NullPointerException if {@code title} is {@code null}.
     */
    void setTitleText(String title);

    /**
     * Sets the message for the progress view.
     * @param message The message.  Not {@code null}.
     * @throws NullPointerException if {@code message} is {@code null}.
     */
    void setMessageText(String message);

    /**
     * Clears the message on the progress view.
     */
    void clearMessageText();
}

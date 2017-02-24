package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/03/16
 */
public interface ForbiddenView extends IsWidget {

    /**
     * Sets a sub-message that will be displayed by the view.  This can be used
     * to indicate why something is forbidden.
     * @param message The message
     */
    void setSubMessage(@Nonnull String message);

    /**
     * Clears the sub-message.
     */
    void clearSubMessage();
}

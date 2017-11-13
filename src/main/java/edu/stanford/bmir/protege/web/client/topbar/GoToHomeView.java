package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public interface GoToHomeView extends IsWidget {

    /**
     * Sets the handler for when "Go to Home" is requested.
     * @param handler The handler.
     */
    void setGoToHomeHandler(@Nonnull GoToHomeHandler handler);
}

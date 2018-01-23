package edu.stanford.bmir.protege.web.client.logout;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/02/16
 */
public interface LogoutView extends IsWidget {

    void setLogoutHandler(LogoutHandler handler);
}

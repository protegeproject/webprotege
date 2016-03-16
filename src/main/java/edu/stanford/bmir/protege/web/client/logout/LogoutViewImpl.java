package edu.stanford.bmir.protege.web.client.logout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/02/16
 */
public class LogoutViewImpl extends Composite implements LogoutView {

    interface LogoutViewImplUiBinder extends UiBinder<HTMLPanel, LogoutViewImpl> {

    }

    private static LogoutViewImplUiBinder ourUiBinder = GWT.create(LogoutViewImplUiBinder.class);

    private LogoutHandler logoutHandler = new LogoutHandler() {
        @Override
        public void handleLogout() {

        }
    };

    @UiField
    protected HasClickHandlers logoutButton;

    @UiHandler("logoutButton")
    protected void handleLogoutButtonClicked(ClickEvent clickEvent) {
        logoutHandler.handleLogout();
    }

    public LogoutViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setLogoutHandler(LogoutHandler handler) {
        this.logoutHandler = handler;
    }
}
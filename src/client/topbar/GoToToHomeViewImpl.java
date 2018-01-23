package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class GoToToHomeViewImpl extends Composite implements GoToHomeView {

    interface GoToHomeViewImplUiBinder extends UiBinder<HTMLPanel, GoToToHomeViewImpl> {

    }

    private static GoToHomeViewImplUiBinder ourUiBinder = GWT.create(GoToHomeViewImplUiBinder.class);


    private GoToHomeHandler goToHomeHandler = () -> {};

    @UiField
    protected HasClickHandlers goToHomeButton;

    @Inject
    public GoToToHomeViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("goToHomeButton")
    protected void handleGoToHome(ClickEvent clickEvent) {
        goToHomeHandler.handleGoToHome();
    }



    @Override
    public void setGoToHomeHandler(@Nonnull GoToHomeHandler handler) {
        this.goToHomeHandler = checkNotNull(handler);
    }
}
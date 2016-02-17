package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.StateChangedHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class PortletUiImpl extends Composite implements PortletUi {

    interface PortletUiImplUiBinder extends UiBinder<HTMLPanel, PortletUiImpl> {

    }

    private static PortletUiImplUiBinder ourUiBinder = GWT.create(PortletUiImplUiBinder.class);


    @UiField
    protected FlowPanel toolbar;

    @UiField
    protected PortletContentHolder contentHolder;

    public PortletUiImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setToolbarVisible(false);
    }

    @Override
    public void setToolbarVisible(boolean visible) {
        toolbar.setVisible(visible);
        if(!visible) {
            contentHolder.getElement().getStyle().setTop(0, Style.Unit.PX);
        }
        else {
            contentHolder.getElement().getStyle().setTop(23, Style.Unit.PX);
        }
    }

    @Override
    public void addPortletAction(final PortletAction action) {
        final Button button = new Button(action.getName());
        button.addStyleName(WebProtegeClientBundle.BUNDLE.buttons().toolbarButton());
        toolbar.add(button);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                action.getActionHandler().handleActionInvoked(action, event);
            }
        });
        action.setStateChangedHandler(new StateChangedHandler<PortletAction>() {
            @Override
            public void handleStateChanged(PortletAction value) {
                button.setEnabled(value.isEnabled());
            }
        });
    }

    @Override
    public AcceptsOneWidget getContentHolder() {
        return contentHolder;
    }
}
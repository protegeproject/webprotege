package edu.stanford.bmir.protege.web.client.ui.library.popupmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class MenuButton extends Composite implements HasClickHandlers {

    interface MenuButtonUiBinder extends UiBinder<HTMLPanel, MenuButton> {

    }

    private static MenuButtonUiBinder ourUiBinder = GWT.create(MenuButtonUiBinder.class);
//
//    @UiField
//    HTMLPanel topBar;
//
//    @UiField
//    HTMLPanel middleBar;
//
//    @UiField
//    HTMLPanel bottomBar;

    public MenuButton() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public void setColor(String color) {
//        topBar.getElement().getStyle().setBackgroundColor(color);
//        middleBar.getElement().getStyle().setBackgroundColor(color);
//        bottomBar.getElement().getStyle().setBackgroundColor(color);
    }

}

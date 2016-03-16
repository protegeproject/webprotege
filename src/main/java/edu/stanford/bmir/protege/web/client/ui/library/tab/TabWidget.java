package edu.stanford.bmir.protege.web.client.ui.library.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.MenuButton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class TabWidget extends Composite implements HasClickHandlers {

    interface TabWidgetUiBinder extends UiBinder<HTMLPanel, TabWidget> {

    }

    @UiField
    protected Label label;

    @UiField
    protected MenuButton menuButton;

    private static TabWidgetUiBinder ourUiBinder = GWT.create(TabWidgetUiBinder.class);

    public TabWidget(String label) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.label.setText(label);
    }

    public String getLabel() {
        return label.getText();
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return menuButton.addClickHandler(handler);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public void setMenuButtonVisible(boolean visible) {
        menuButton.setVisible(visible);
    }

    public UIObject getMenuButton() {
        return menuButton;
    }
}

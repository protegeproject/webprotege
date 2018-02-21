package edu.stanford.bmir.protege.web.client.library.popupmenu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.action.UIAction;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/03/16
 */
public class MenuItem extends Composite implements HasEnabled, HasText {

    public static final String DISABLED = "disabled";

    interface MenuItemUiBinder extends UiBinder<HTMLPanel, MenuItem> {

    }

    private static MenuItemUiBinder ourUiBinder = GWT.create(MenuItemUiBinder.class);

    @UiField
    Label labelField;

    private final UIAction uiAction;

    public MenuItem(final UIAction action) {
        this.uiAction = action;
        this.uiAction.setStateChangedHandler(a -> {
            setEnabled(a.isEnabled());
            setVisible(a.isVisible());
        });
        this.uiAction.setLabelChangedHandler(a -> setText(a.getLabel()));
        initWidget(ourUiBinder.createAndBindUi(this));
        setText(action.getLabel());
        setEnabled(action.isEnabled());
    }

    public void execute() {
        if (isEnabled()) {
            uiAction.execute();
        }
    }

    @Override
    public boolean isEnabled() {
        return  !getStyleName().contains(BUNDLE.menu().popupMenuItemDisabled());
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(enabled) {
            removeStyleName(BUNDLE.menu().popupMenuItemDisabled());
        }
        else {
            addStyleName(BUNDLE.menu().popupMenuItemDisabled());
        }
    }

    @Override
    public String getText() {
        return labelField.getText();
    }

    @Override
    public void setText(String text) {
        labelField.setText(text);
    }
}
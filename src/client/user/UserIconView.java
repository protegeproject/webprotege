package edu.stanford.bmir.protege.web.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class UserIconView extends Composite {

    interface UserIconImplUiBinder extends UiBinder<HTMLPanel, UserIconView> {

    }

    private static UserIconImplUiBinder ourUiBinder = GWT.create(UserIconImplUiBinder.class);

    public UserIconView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected HasText label;

    public void setText(String text) {
        label.setText(text);
    }

    public void setColor(String color) {
        getElement().getStyle().setBackgroundColor(color);
    }
}
package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeListSeparator extends Composite {
    interface ChangeListSeparatorUiBinder extends UiBinder<HTMLPanel, ChangeListSeparator> {
    }

    private static ChangeListSeparatorUiBinder ourUiBinder = GWT.create(ChangeListSeparatorUiBinder.class);

    public ChangeListSeparator() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected HasText separatorText;

    public void setSeparatorText(String text) {
        separatorText.setText(text);
    }
}
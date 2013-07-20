package edu.stanford.bmir.protege.web.client.ui.library.progress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */
public class ProgressBarViewImpl extends Composite implements ProgressBarView {

    interface ProgressBarViewImplUiBinder extends UiBinder<HTMLPanel, ProgressBarViewImpl> {

    }

    private static ProgressBarViewImplUiBinder ourUiBinder = GWT.create(ProgressBarViewImplUiBinder.class);

    @UiField
    protected HasText titleText;

    @UiField
    protected HasText messageText;

    public ProgressBarViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setTitleText(String title) {
        titleText.setText(checkNotNull(title));
    }

    @Override
    public void setMessageText(String message) {
        messageText.setText(checkNotNull(message));
    }

    @Override
    public void clearMessageText() {
        messageText.setText("");
    }
}
package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Image;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public class MessageBoxViewImpl extends Composite implements MessageBoxView {

    interface MessageBoxViewImplUiBinder extends UiBinder<HTMLPanel, MessageBoxViewImpl> {

    }

    private static MessageBoxViewImplUiBinder ourUiBinder = GWT.create(MessageBoxViewImplUiBinder.class);

    @UiField
    protected HasHTML titleLabel;

    @UiField
    protected HasHTML messageLabel;

    @UiField
    protected Image iconImage;

//    @UiField
//    protected Widget iconHolder;

    public MessageBoxViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public void setMessageStyle(MessageStyle messageStyle) {
        Optional<String> iconURL = messageStyle.getUrl();
        if(iconURL.isPresent()) {
            iconImage.getElement().getStyle().setOpacity(1);
            iconImage.setUrl(iconURL.get());
//            iconHolder.getElement().getStyle().clearDisplay();
        }
        else {
//            iconHolder.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }

    @Override
    public void setMainMessage(String title) {
        titleLabel.setHTML(title);
    }

    @Override
    public void setSubMessage(String message) {
        messageLabel.setHTML(message);
    }
}
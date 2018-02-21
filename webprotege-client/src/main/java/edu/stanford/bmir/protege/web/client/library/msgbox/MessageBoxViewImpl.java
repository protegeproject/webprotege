package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Image;

import java.util.Optional;

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

    public MessageBoxViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public void setMessageStyle(MessageStyle messageStyle) {
        Optional<DataResource> res = messageStyle.getImage();
        res.ifPresent(dataResource -> {
            iconImage.getElement().getStyle().setOpacity(1);
            iconImage.setUrl(dataResource.getSafeUri().asString());
        });
        if(!res.isPresent()) {
            iconImage.getElement().getStyle().setOpacity(0);
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
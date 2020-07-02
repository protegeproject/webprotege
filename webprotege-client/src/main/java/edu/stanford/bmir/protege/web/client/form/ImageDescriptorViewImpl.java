package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-19
 */
public class ImageDescriptorViewImpl extends Composite implements ImageDescriptorView {

    interface ImageDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, ImageDescriptorViewImpl> {

    }

    private static ImageDescriptorViewImplUiBinder ourUiBinder = GWT.create(ImageDescriptorViewImplUiBinder.class);

    @Inject
    public ImageDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}

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
public class NoControlDescriptorViewImpl extends Composite implements NoControlDescriptorView {

    interface NoControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, NoControlDescriptorViewImpl> {

    }

    private static NoControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(NoControlDescriptorViewImplUiBinder.class);

    @Inject
    public NoControlDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}

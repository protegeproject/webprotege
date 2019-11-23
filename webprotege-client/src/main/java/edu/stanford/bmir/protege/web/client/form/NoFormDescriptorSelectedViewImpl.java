package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-22
 */
public class NoFormDescriptorSelectedViewImpl extends Composite implements NoFormDescriptorSelectedView {

    interface NoFormSelectedViewImplUiBinder extends UiBinder<HTMLPanel, NoFormDescriptorSelectedViewImpl> {

    }

    private static NoFormSelectedViewImplUiBinder ourUiBinder = GWT.create(NoFormSelectedViewImplUiBinder.class);

    @Inject
    public NoFormDescriptorSelectedViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}

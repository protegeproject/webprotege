package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class NoFormViewImpl extends Composite implements NoFormView {

    interface NoFormViewImplUiBinder extends UiBinder<HTMLPanel, NoFormViewImpl> {

    }

    private static NoFormViewImplUiBinder ourUiBinder = GWT.create(NoFormViewImplUiBinder.class);

    @Inject
    public NoFormViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}

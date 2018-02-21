package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2017
 */
public class NothingSelectedViewImpl extends Composite implements NothingSelectedView {

    interface NothingSelectedViewImplUiBinder extends UiBinder<HTMLPanel, NothingSelectedViewImpl> {

    }

    private static NothingSelectedViewImplUiBinder ourUiBinder = GWT.create(NothingSelectedViewImplUiBinder.class);

    @Inject
    public NothingSelectedViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
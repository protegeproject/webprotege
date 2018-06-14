package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class BlankCriteriaViewImpl extends Composite implements BlankCriteriaView {

    interface BlankCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, BlankCriteriaViewImpl> {

    }

    private static BlankCriteriaViewImplUiBinder ourUiBinder = GWT.create(BlankCriteriaViewImplUiBinder.class);

    @Inject
    public BlankCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
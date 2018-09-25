package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class ReplaceAnnotationsValueViewImpl extends Composite {

    interface ReplaceAnnotationsValueViewImplUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, edu.stanford.bmir.protege.web.client.bulkop.ReplaceAnnotationsValueViewImpl> {

    }

    private static ReplaceAnnotationsValueViewImplUiBinder ourUiBinder = GWT.create(ReplaceAnnotationsValueViewImplUiBinder.class);

    public ReplaceAnnotationsValueViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
package edu.stanford.bmir.protege.web.client.progress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVisibility;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2017
 */
public class BusyView extends Composite implements HasVisibility {

    interface BusyViewUiBinder extends UiBinder<HTMLPanel, BusyView> {

    }

    private static BusyViewUiBinder ourUiBinder = GWT.create(BusyViewUiBinder.class);

    public BusyView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
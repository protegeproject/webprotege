package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class CouldNotFindPortletWidget extends Composite {

    interface CouldNotFindPortletWidgetUiBinder extends UiBinder<HTMLPanel, CouldNotFindPortletWidget> {

    }

    private static CouldNotFindPortletWidgetUiBinder ourUiBinder = GWT.create(CouldNotFindPortletWidgetUiBinder.class);

    public CouldNotFindPortletWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
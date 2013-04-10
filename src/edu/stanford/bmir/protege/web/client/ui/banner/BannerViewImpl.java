package edu.stanford.bmir.protege.web.client.ui.banner;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/01/2013
 */
public class BannerViewImpl extends Composite {

    interface BannerUiBinder extends UiBinder<HTMLPanel, BannerViewImpl> {

    }

    private static BannerUiBinder ourUiBinder = GWT.create(BannerUiBinder.class);

    public BannerViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

    }
}
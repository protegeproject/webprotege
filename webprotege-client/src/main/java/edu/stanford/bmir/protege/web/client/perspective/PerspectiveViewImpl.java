package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class PerspectiveViewImpl extends Composite implements PerspectiveView {

    interface PerspectiveViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveViewImpl> {

    }

    private static PerspectiveViewImplUiBinder ourUiBinder = GWT.create(PerspectiveViewImplUiBinder.class);

    @UiField
    protected SimpleLayoutPanel mainPanel;

    @Inject
    public PerspectiveViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setWidget(IsWidget w) {
        GWT.log("[PerspectiveViewImpl] setWidget: " + w.hashCode());
        mainPanel.setWidget(w);
    }

    @Override
    public void onResize() {
        LayoutUtil.setBounds(mainPanel, 0, 0, 0, 0);
        mainPanel.onResize();
    }
}
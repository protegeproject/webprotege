package edu.stanford.bmir.protege.web.client.workspace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.banner.BannerPresenter;
import edu.stanford.bmir.protege.web.client.banner.BannerViewImpl;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class WorkspaceViewImpl extends Composite implements WorkspaceView {

    interface WorkspaceViewImplUiBinder extends UiBinder<HTMLPanel, WorkspaceViewImpl> {

    }

    private static WorkspaceViewImplUiBinder ourUiBinder = GWT.create(WorkspaceViewImplUiBinder.class);

    @UiField(provided = true)
    protected BannerViewImpl bannerView;

    public WorkspaceViewImpl() {
        BannerPresenter bannerPresenter = new BannerPresenter();
        bannerView = (BannerViewImpl) bannerPresenter.getView();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

}
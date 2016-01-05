package edu.stanford.bmir.protege.web.client.workspace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.banner.BannerPresenter;
import edu.stanford.bmir.protege.web.client.banner.BannerViewImpl;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.ProjectDisplayContainerPanel;

import javax.inject.Inject;

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

    @UiField(provided = true)
    protected GwtExtAdapterPanel adapterPanel;

    @Inject
    public WorkspaceViewImpl(ProjectDisplayContainerPanel projectDisplayContainerPanel, BannerPresenter bannerPresenter) {
        bannerView = (BannerViewImpl) bannerPresenter.getView();
        adapterPanel = new GwtExtAdapterPanel(projectDisplayContainerPanel);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

}
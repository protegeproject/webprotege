package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
public class ProjectViewImpl extends Composite implements ProjectView, HasSelectionHandlers<PerspectiveImpl> {

    interface ProjectViewImplUiBinder extends UiBinder<HTMLPanel, ProjectViewImpl> {

    }

    private static ProjectViewImplUiBinder ourUiBinder = GWT.create(ProjectViewImplUiBinder.class);


    @UiField
    protected SimpleLayoutPanel topBar;

    @UiField
    protected SimpleLayoutPanel perspectiveLinkBarViewContainer;

    @UiField(provided = true)
    protected SimpleLayoutPanel perspectiveViewContainer = new SimpleLayoutPanel() {
        @Override
        public void setWidget(Widget w) {
            GWT.log("[ProjectViewImpl] setWidget: " + w.hashCode());
            super.setWidget(w);
        }
    };


    public ProjectViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public AcceptsOneWidget getTopBarContainer() {
        return topBar;
    }

    @Override
    public AcceptsOneWidget getPerspectiveLinkBarViewContainer() {
        return perspectiveLinkBarViewContainer;
    }

    @Override
    public AcceptsOneWidget getPerspectiveViewContainer() {
        return perspectiveViewContainer;
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<PerspectiveImpl> handler) {
        return addHandler(handler, SelectionEvent.<PerspectiveImpl>getType());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void onResize() {
        LayoutUtil.setBounds(this, 0, 0, 0, 0);
        LayoutUtil.setFill(topBar, 0, 35);
        LayoutUtil.setFill(perspectiveLinkBarViewContainer, 36, 24);
        LayoutUtil.setBounds(perspectiveViewContainer, 60, 0, 0, 0);
        topBar.onResize();
        perspectiveLinkBarViewContainer.onResize();
        perspectiveViewContainer.onResize();
    }
}
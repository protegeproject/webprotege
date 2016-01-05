package edu.stanford.bmir.protege.web.client.banner;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.actionbar.application.ApplicationActionBar;
import edu.stanford.bmir.protege.web.client.actionbar.application.ApplicationActionBarImpl;
import edu.stanford.bmir.protege.web.client.actionbar.project.ProjectActionBar;
import edu.stanford.bmir.protege.web.client.actionbar.project.ProjectActionBarImpl;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class BannerViewImpl extends Composite implements BannerView {

    interface BannerViewImplUiBinder extends UiBinder<HTMLPanel, BannerViewImpl> {

    }

    private static BannerViewImplUiBinder ourUiBinder = GWT.create(BannerViewImplUiBinder.class);


    @UiField(provided = true)
    protected ApplicationActionBarImpl applicationActionBar;

    @UiField(provided = true)
    protected ProjectActionBarImpl projectActionBar;


    @Inject
    public BannerViewImpl(ApplicationActionBarImpl applicationActionBar, ProjectActionBarImpl projectActionBar) {
        this.applicationActionBar = applicationActionBar;
        this.projectActionBar = projectActionBar;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public ApplicationActionBar getApplicationActionBar() {
        return applicationActionBar;
    }

    @Override
    public ProjectActionBar getProjectActionBar() {
        return projectActionBar;
    }
}
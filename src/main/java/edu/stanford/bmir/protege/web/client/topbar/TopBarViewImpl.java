package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class TopBarViewImpl extends Composite implements TopBarView {

    interface TopBarViewImplUiBinder extends UiBinder<HTMLPanel, TopBarViewImpl> {

    }

    private static TopBarViewImplUiBinder ourUiBinder = GWT.create(TopBarViewImplUiBinder.class);

    public TopBarViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected AcceptsOneWidget homeContainer;

    @UiField
    protected AcceptsOneWidget projectMenuContainer;

    @UiField
    protected AcceptsOneWidget sharingSettingsContainer;

    @UiField
    protected AcceptsOneWidget loggedInUserContainer;

    @UiField
    protected AcceptsOneWidget helpContainer;

    @Override
    public AcceptsOneWidget getGoToHomeWidgetContainer() {
        return homeContainer;
    }

    @Override
    public void addToLeft(IsWidget widget) {

    }

    @Override
    public void addToCenter(IsWidget widget) {

    }

    @Override
    public void addToRight(IsWidget widget) {

    }

    public AcceptsOneWidget getProjectMenuContainer() {
        return projectMenuContainer;
    }

    @Override
    public AcceptsOneWidget getSharingSettingsContainer() {
        return sharingSettingsContainer;
    }

    @Override
    public AcceptsOneWidget getLoggedInUserWidgetContainer() {
        return loggedInUserContainer;
    }

    @Override
    public AcceptsOneWidget getHelpWidgetContainer() {
        return helpContainer;
    }
}
package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class TopBarViewImpl extends Composite implements TopBarView {

    interface TopBarViewImplUiBinder extends UiBinder<HTMLPanel, TopBarViewImpl> {

    }

    private static TopBarViewImplUiBinder ourUiBinder = GWT.create(TopBarViewImplUiBinder.class);

    @Inject
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

    @UiField
    protected HasText projectTitle;

    @UiField
    SimplePanel preferredLanguageContainer;

    @Override
    public AcceptsOneWidget getGoToHomeContainer() {
        return homeContainer;
    }

    @Override
    public void setProjectTitle(@Nonnull String projectTitle) {
        this.projectTitle.setText(projectTitle);
    }

    @Override
    public void clearProjectTitle() {
        this.projectTitle.setText("");
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
    public AcceptsOneWidget getLoggedInUserContainer() {
        return loggedInUserContainer;
    }

    @Override
    public AcceptsOneWidget getHelpContainer() {
        return helpContainer;
    }

    @Override
    public AcceptsOneWidget getPreferredLanguageContainer() {
        return preferredLanguageContainer;
    }
}
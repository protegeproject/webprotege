package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.progress.BusyViewImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public class SettingsViewImpl extends Composite implements SettingsView {

    interface SettingsViewImplUiBinder extends UiBinder<HTMLPanel, SettingsViewImpl> {

    }

    private static SettingsViewImplUiBinder ourUiBinder = GWT.create(SettingsViewImplUiBinder.class);

    @UiField
    Button cancelButton;

    @UiField
    Button applyButton;

    @UiField
    Label projectTitle;

    @UiField
    HTMLPanel settingsContainer;

    @UiField
    Label settingsTitle;

    @UiField
    BusyViewImpl busyView;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private ApplySettingsHandler applyHandler = () -> {};

    @Nonnull
    private CancelSettingsHandler cancelHandler = () -> {};

    @Inject
    public SettingsViewImpl(@Nonnull Messages messages) {
        this.messages = checkNotNull(messages);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
    }

    @Override
    public void clearView() {
        settingsContainer.clear();
    }

    @UiHandler("applyButton")
    protected void handleApplyChanges(ClickEvent event) {
        GWT.log("[SettingsViewImpl] Apply Button Clicked");
        applyHandler.handleApplySettings();
    }

    @UiHandler("cancelButton")
    protected void handleCancelChanges(ClickEvent event) {
        cancelHandler.handleCancelSettings();
    }

    @Override
    public void setProjectTitle(@Nonnull String projectTitle) {
        this.projectTitle.setText(checkNotNull(projectTitle));
    }

    @Override
    public void setApplyButtonVisible(boolean visible) {
        applyButton.setVisible(visible);
    }

    @Override
    public void setCancelButtonVisible(boolean visible) {
        cancelButton.setVisible(visible);
    }

    @Override
    public void addSectionViewContainer(@Nonnull SettingsSectionViewContainer view) {
        settingsContainer.add(view);
    }

    @Override
    public void setApplyButtonText(@Nonnull String text) {
        this.applyButton.setText(checkNotNull(text));
    }

    @Override
    public void setCancelButtonText(@Nonnull String text) {
        this.cancelButton.setText(checkNotNull(text));
    }

    @Override
    public void setApplySettingsHandler(@Nonnull ApplySettingsHandler applyButtonHandler) {
        GWT.log("[SettingsViewImpl] Setting apply settings handler");
        this.applyHandler = checkNotNull(applyButtonHandler);
    }

    @Override
    public void setCancelSettingsHandler(@Nonnull CancelSettingsHandler cancelButtonHandler) {
        this.cancelHandler = checkNotNull(cancelButtonHandler);
    }

    @Override
    public void showErrorMessage(@Nonnull String msgTitle, @Nonnull String msgBody) {

    }

    @Override
    public void setApplySettingsStarted() {

    }

    @Override
    public void setApplySettingsFinished() {

    }

    @Override
    public void setSettingsTitle(@Nonnull String settingsTitle) {
        this.settingsTitle.setText(checkNotNull(settingsTitle).trim());
    }

    @Override
    public void setBusy(boolean busy) {
        busyView.setVisible(busy);
    }
}

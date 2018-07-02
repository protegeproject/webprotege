package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;

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

    @Nonnull
    private final Messages messages;

    @Nonnull
    private ApplyButtonHandler applyHandler = () -> {};

    @Nonnull
    private CancelButtonHandler cancelHandler = () -> {};

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
        applyHandler.handleApplyClicked();
    }

    @UiHandler("cancelButton")
    protected void handleCancelChanges(ClickEvent event) {
        cancelHandler.handleCancelClicked();
    }

    @Override
    public void setProjectTitle(@Nonnull String projectTitle) {
        this.projectTitle.setText(checkNotNull(projectTitle));
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
    public void setApplyButtonHandler(@Nonnull ApplyButtonHandler applyButtonHandler) {
        this.applyHandler = checkNotNull(applyButtonHandler);
    }

    @Override
    public void setCancelButtonHandler(@Nonnull CancelButtonHandler cancelButtonHandler) {
        this.cancelHandler = checkNotNull(cancelButtonHandler);
    }
}

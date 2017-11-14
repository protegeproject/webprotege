package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.SlackIntegrationSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSetting;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSettings;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class ProjectSettingsViewImpl extends Composite implements ProjectSettingsView  {

    interface ProjectSettingsViewImplUiBinder extends UiBinder<HTMLPanel, ProjectSettingsViewImpl> {
    }

    private static ProjectSettingsViewImplUiBinder ourUiBinder = GWT.create(ProjectSettingsViewImplUiBinder.class);

    @UiField
    protected TextBox displayNameField;

    @UiField
    protected TextArea descriptionField;

    @UiField
    protected HasClickHandlers applyButton;

    @UiField
    protected Button cancelButton;

    @UiField
    TextBox slackPayloadUrl;

    @UiField(provided = true)
    ValueListEditorImpl<String> webhooks;

    @UiField
    Label projectTitle;

    private ApplyChangesHandler applyChangesHandler = () -> {};

    private CancelChangedHandler cancelChangesHandler = () -> {};


    private Optional<ProjectSettings> pristineValue = Optional.empty();

    @Inject
    public ProjectSettingsViewImpl() {
        webhooks = new ValueListEditorImpl<>(WebhookViewImpl::new);
        webhooks.setEnabled(true);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setProjectTitle(@Nonnull String projectTitle) {
        this.projectTitle.setText(checkNotNull(projectTitle));
    }

    @UiHandler("applyButton")
    protected void handleApplyButtonClicked(ClickEvent event) {
        applyChangesHandler.handleApplyChanges();
    }

    @UiHandler("cancelButton")
    protected void handleCancelButtonClicked(ClickEvent event) {
        cancelChangesHandler.handleCancelChanges();
    }

    @Override
    public void setCancelButtonVisible(boolean visible) {
        cancelButton.setVisible(visible);
    }

    @Override
    public void setApplyChangesHandler(@Nonnull ApplyChangesHandler applyChangesHandler) {
        this.applyChangesHandler = checkNotNull(applyChangesHandler);
    }

    @Override
    public void setCancelChangesHandler(@Nonnull CancelChangedHandler cancelChangesHandler) {
        this.cancelChangesHandler = checkNotNull(cancelChangesHandler);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> displayNameField.setFocus(true));
    }

    @Override
    public void setValue(ProjectSettings object) {
        pristineValue = Optional.of(object);
        displayNameField.setText(object.getProjectDisplayName());
        descriptionField.setText(object.getProjectDescription());
        slackPayloadUrl.setText(object.getSlackIntegrationSettings().getPayloadUrl());
        webhooks.clearValue();
        List<String> payloadUrls = object.getWebhookSettings().getWebhookSettings().stream()
                .map(WebhookSetting::getPayloadUrl)
                .collect(toList());
        webhooks.setValue(payloadUrls);
    }

    @Override
    public void clearValue() {
        pristineValue = Optional.empty();
        displayNameField.setText("");
        descriptionField.setText("");
    }

    @Override
    public Optional<ProjectSettings> getValue() {
        if(pristineValue.isPresent()) {
            ProjectSettings oldValue = pristineValue.get();
            return Optional.of(new ProjectSettings(oldValue.getProjectId(),
                                                   getDisplayName(),
                                                   getDescription(),
                                                   getSlackIntegrationSettings(),
                                                   getWebhookSettings()));
        }
        else {
            return Optional.empty();
        }
    }

    private WebhookSettings getWebhookSettings() {
        Optional<List<String>> value = webhooks.getValue();
        if(value.isPresent()) {
            List<WebhookSetting> settings = value.get().stream()
                    .map(u -> new WebhookSetting(u, Sets.newHashSet(ProjectWebhookEventType.values())))
                    .collect(toList());
            return new WebhookSettings(settings);
        }
        else {
            return new WebhookSettings(Collections.emptyList());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ProjectSettings>> optionalValueChangeHandler) {
        return addHandler(optionalValueChangeHandler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return pristineValue.isPresent();
    }


    private String getDisplayName() {
        return displayNameField.getText().trim();
    }

    private String getDescription() {
        return descriptionField.getText().trim();
    }

    private SlackIntegrationSettings getSlackIntegrationSettings() {
        return new SlackIntegrationSettings(slackPayloadUrl.getText().trim());
    }
}
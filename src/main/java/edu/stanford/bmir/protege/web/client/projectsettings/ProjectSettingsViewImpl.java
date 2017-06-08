package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.base.Optional;
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
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.SlackIntegrationSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

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
    TextBox slackPayloadUrl;

    private ApplyChangesHandler applyChangesHandler = () -> {};

    private java.util.Optional<ProjectSettings> pristineValue = java.util.Optional.empty();

    @Inject
    public ProjectSettingsViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("applyButton")
    protected void handleApplyButtonClicked(ClickEvent event) {
        applyChangesHandler.handleApplyChanges();
    }

    @Override
    public void setApplyChangesHandler(@Nonnull ApplyChangesHandler applyChangesHandler) {
        this.applyChangesHandler = checkNotNull(applyChangesHandler);
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
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> displayNameField.setFocus(true));
    }

    @Override
    public void setValue(ProjectSettings object) {
        pristineValue = java.util.Optional.of(object);
        displayNameField.setText(object.getProjectDisplayName());
        descriptionField.setText(object.getProjectDescription());
        slackPayloadUrl.setText(object.getSlackIntegrationSettings().getPayloadUrl());
    }

    @Override
    public void clearValue() {
        pristineValue = java.util.Optional.empty();
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
                                                   getSlackIntegrationSettings()));
        }
        else {
            return Optional.absent();
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
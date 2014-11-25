package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;

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


    private Optional<ProjectSettings> pristineValue = Optional.absent();

    public ProjectSettingsViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
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
    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(displayNameField);
    }

    @Override
    public void setValue(ProjectSettings object) {
        pristineValue = Optional.of(object);
        displayNameField.setText(object.getProjectDisplayName());
        descriptionField.setText(object.getProjectDescription());
    }

    @Override
    public void clearValue() {
        pristineValue = Optional.absent();
        displayNameField.setText("");
        descriptionField.setText("");
    }

    @Override
    public Optional<ProjectSettings> getValue() {
        if(pristineValue.isPresent()) {
            ProjectSettings oldValue = pristineValue.get();
            return Optional.of(new ProjectSettings(oldValue.getProjectId(), oldValue.getProjectType(), getDisplayName(), getDescription()));
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
}
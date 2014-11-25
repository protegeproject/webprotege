package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.SerializableEvent;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class ProjectSettingsChangedEvent extends SerializableEvent<ProjectSettingsChangedHandler> {

    private static final transient Type<ProjectSettingsChangedHandler> TYPE = new Type<ProjectSettingsChangedHandler>();

    public static Type<ProjectSettingsChangedHandler> getType() {
        return TYPE;
    }

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private ProjectSettingsChangedEvent() {
    }

    public ProjectSettingsChangedEvent(ProjectSettings projectSettings) {
        this.projectSettings = checkNotNull(projectSettings);
    }

    /**
     * Gets the project settings.
     * @return The project settings.  Not {@code null}.
     */
    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    @Override
    public Type<ProjectSettingsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProjectSettingsChangedHandler projectSettingsChangedHandler) {
        projectSettingsChangedHandler.handleProjectSettingsChanged(this);
    }
}

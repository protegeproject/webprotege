package edu.stanford.bmir.protege.web.shared.lang;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class DisplayNameSettingsChangedEvent extends ProjectEvent<DisplayNameSettingsChangedHandler> {

    public static final transient Event.Type<DisplayNameSettingsChangedHandler> ON_DISPLAY_LANGUAGE_CHANGED = new Event.Type<>();

    @Nonnull
    private final DisplayNameSettings displayNameSettings;

    private DisplayNameSettingsChangedEvent(@Nonnull ProjectId source,
                                            @Nonnull DisplayNameSettings displayNameSettings) {
        super(source);
        this.displayNameSettings = checkNotNull(displayNameSettings);
    }

    public static DisplayNameSettingsChangedEvent get(@Nonnull ProjectId projectId,
                                                      @Nonnull DisplayNameSettings displayNameSettings) {
        return new DisplayNameSettingsChangedEvent(projectId, displayNameSettings);
    }

    @Nonnull
    public static Event.Type<DisplayNameSettingsChangedHandler> getType() {
        return ON_DISPLAY_LANGUAGE_CHANGED;
    }

    @Override
    public Event.Type<DisplayNameSettingsChangedHandler> getAssociatedType() {
        return ON_DISPLAY_LANGUAGE_CHANGED;
    }

    @Override
    protected void dispatch(DisplayNameSettingsChangedHandler handler) {
        handler.handleDisplayNameSettingsChanged(this);
    }

    @Nonnull
    public DisplayNameSettings getDisplayLanguage() {
        return displayNameSettings;
    }
}

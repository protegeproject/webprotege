package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 *
 * Manages that project that is active in the user interface.  If the active project
 * changes then an {@link ActiveProjectChangedEvent}
 * is fired on the event bus.
 */
public class ActiveProjectManagerImpl implements ActiveProjectManager {

    private final EventBus eventBus;

    private final PlaceController placeController;

    private Optional<ProjectId> activeProject = Optional.empty();

    @Inject
    public ActiveProjectManagerImpl(EventBus eventBus, PlaceController placeController) {
        this.eventBus = checkNotNull(eventBus);
        this.placeController = placeController;
        eventBus.addHandler(PlaceChangeEvent.TYPE, event -> handlePlaceChange());
    }

    @Nonnull
    @Override
    public Optional<ProjectId> getActiveProjectId() {
        Place place = placeController.getWhere();
        if(!(place instanceof HasProjectId)) {
            return Optional.empty();
        }
        HasProjectId projectViewPlace = (HasProjectId) place;
        return Optional.of(projectViewPlace.getProjectId());
    }

    private void handlePlaceChange() {
        Optional<ProjectId> projectId = getActiveProjectId();
        if(!projectId.equals(activeProject)) {
            activeProject = projectId;
            eventBus.fireEvent(new ActiveProjectChangedEvent(activeProject).asGWTEvent());
        }
    }
}

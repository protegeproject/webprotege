package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.GetProjectDetailsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

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

    private final DispatchServiceManager dispatchServiceManager;

    private Optional<ProjectId> activeProject = Optional.empty();

    private Optional<ProjectDetails> cachedProjectDetails = Optional.empty();

    @Inject
    public ActiveProjectManagerImpl(@Nonnull EventBus eventBus,
                                    @Nonnull PlaceController placeController,
                                    @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.eventBus = checkNotNull(eventBus);
        this.placeController = placeController;
        this.dispatchServiceManager = dispatchServiceManager;
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

    @Override
    public void getActiveProjectDetails(Consumer<Optional<ProjectDetails>> projectDetailsConsumer) {
        if(cachedProjectDetails.isPresent()) {
            projectDetailsConsumer.accept(cachedProjectDetails);
        }
        else {
            Optional<ProjectId> activeProjectId = getActiveProjectId();
            if(activeProjectId.isPresent()) {
                dispatchServiceManager.execute(new GetProjectDetailsAction(activeProjectId.get()), result -> {
                    GWT.log("[ActiveProjectManagerImpl] Got details: " + result.getProjectDetails());
                    cachedProjectDetails = Optional.of(result.getProjectDetails());
                    projectDetailsConsumer.accept(cachedProjectDetails);
                });
            }
            else {
                projectDetailsConsumer.accept(Optional.empty());
            }
        }


    }

    private void handlePlaceChange() {
        Optional<ProjectId> projectId = getActiveProjectId();
        if(!projectId.equals(activeProject)) {
            cachedProjectDetails = Optional.empty();
            activeProject = projectId;
            eventBus.fireEvent(new ActiveProjectChangedEvent(activeProject).asGWTEvent());
        }
    }
}

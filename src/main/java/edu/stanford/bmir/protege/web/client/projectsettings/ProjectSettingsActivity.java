package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2017
 */
public class ProjectSettingsActivity extends AbstractActivity {

    private final ProjectSettingsPresenter presenter;

    private final Optional<Place> nextPlace;

    @Inject
    public ProjectSettingsActivity(@Nonnull ProjectSettingsPresenter presenter,
                                   @Nonnull Optional<Place> nextPlace) {
        this.presenter = checkNotNull(presenter);
        this.nextPlace = checkNotNull(nextPlace);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.setNextPlace(nextPlace);
        presenter.start(panel);
    }
}

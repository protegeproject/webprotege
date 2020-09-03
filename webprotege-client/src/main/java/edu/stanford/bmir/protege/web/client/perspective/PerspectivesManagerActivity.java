package edu.stanford.bmir.protege.web.client.perspective;

import com.google.auto.value.AutoValue;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
@AutoValue
public abstract class PerspectivesManagerActivity extends AbstractActivity {

    public static PerspectivesManagerActivity get(ProjectId projectId, @Nullable Place nextPlaceInternal,
                                                  PerspectivesManagerPresenter perspectivesManagerPresenter) {
        return new AutoValue_PerspectivesManagerActivity(projectId, nextPlaceInternal, perspectivesManagerPresenter);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nullable
    protected abstract Place getNextPlaceInternal();

    @Nonnull
    public Optional<Place> getNextPlace() {
        return Optional.ofNullable(getNextPlaceInternal());
    }

    @Nonnull
    public abstract PerspectivesManagerPresenter getPerspectivesManagerPresenter();

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        PerspectivesManagerPresenter perspectivesManagerPresenter = getPerspectivesManagerPresenter();
        getNextPlace().ifPresent(perspectivesManagerPresenter::setNextPlace);
        perspectivesManagerPresenter.start(panel);
    }
}

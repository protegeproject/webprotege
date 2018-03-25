package edu.stanford.bmir.protege.web.client.tag;

import com.google.common.base.Objects;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class ProjectTagsActivity extends AbstractActivity implements HasProjectId {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectTagsPresenter presenter;

    @Nonnull
    private Optional<Place> nextPlace;

    public ProjectTagsActivity(@Nonnull ProjectId projectId,
                               @Nonnull ProjectTagsPresenter presenter,
                               @Nonnull Optional<Place> nextPlace) {
        this.projectId = checkNotNull(projectId);
        this.presenter = checkNotNull(presenter);
        this.nextPlace = checkNotNull(nextPlace);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.setNextPlace(nextPlace);
        presenter.start(panel, eventBus);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, presenter);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectTagsActivity)) {
            return false;
        }
        ProjectTagsActivity other = (ProjectTagsActivity) obj;
        return this.projectId.equals(other.projectId)
                && this.presenter.equals(other.presenter);
    }
}

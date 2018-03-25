package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class ProjectTagsPlace extends Place implements HasProjectId {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Optional<Place> nextPlace;

    public ProjectTagsPlace(@Nonnull ProjectId projectId,
                            @Nonnull Optional<Place> nextPlace) {
        this.projectId = checkNotNull(projectId);
        this.nextPlace = checkNotNull(nextPlace);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public Optional<Place> getNextPlace() {
        return nextPlace;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectTagsPlace)) {
            return false;
        }
        ProjectTagsPlace other = (ProjectTagsPlace) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectTagsPlace")
                .addValue(projectId)
                .toString();
    }
}

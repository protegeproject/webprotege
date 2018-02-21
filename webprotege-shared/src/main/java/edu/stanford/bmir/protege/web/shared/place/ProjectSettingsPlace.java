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
 * 7 Jun 2017
 */
public class ProjectSettingsPlace extends Place implements HasProjectId {

    private final ProjectId projectId;

    private final Optional<Place> nextPlace;

    public ProjectSettingsPlace(@Nonnull ProjectId projectId,
                                @Nonnull Optional<Place> nextPlace) {
        this.projectId = checkNotNull(projectId);
        this.nextPlace = checkNotNull(nextPlace);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public Optional<Place> getNextPlace() {
        return nextPlace;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, nextPlace);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectSettingsPlace)) {
            return false;
        }
        ProjectSettingsPlace other = (ProjectSettingsPlace) obj;
        return this.projectId.equals(other.projectId)
                && this.nextPlace.equals(other.nextPlace);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectSettingsPlace")
                .addValue(projectId)
                .toString();
    }
}

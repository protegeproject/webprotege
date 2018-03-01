package edu.stanford.bmir.protege.web.shared.place;

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
 * 26 Feb 2018
 */
public class ProjectPrefixDeclarationsPlace extends Place implements HasProjectId {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Optional<Place> nextPlace;

    public ProjectPrefixDeclarationsPlace(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
        this.nextPlace = Optional.empty();
    }

    public ProjectPrefixDeclarationsPlace(@Nonnull ProjectId projectId,
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
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectPrefixDeclarationsPlace)) {
            return false;
        }
        ProjectPrefixDeclarationsPlace other = (ProjectPrefixDeclarationsPlace) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectPrefixDeclarationsPlace")
                .addValue(projectId)
                .toString();
    }
}

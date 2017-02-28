package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingSettingsPlace extends Place implements HasProjectId {

    private final ProjectId projectId;

    private Optional<Place> continueTo = Optional.absent();

    public SharingSettingsPlace(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public Optional<Place> getContinueTo() {
        return continueTo;
    }

    public void setContinueTo(Optional<Place> continueTo) {
        this.continueTo = continueTo;
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
        if (!(obj instanceof SharingSettingsPlace)) {
            return false;
        }
        SharingSettingsPlace other = (SharingSettingsPlace) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("SharingSettingsPlace")
                .addValue(projectId)
                .toString();
    }
}

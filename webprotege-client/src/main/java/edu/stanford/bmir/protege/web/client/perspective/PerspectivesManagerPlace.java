package edu.stanford.bmir.protege.web.client.perspective;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
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
public abstract class PerspectivesManagerPlace extends Place implements HasProjectId {

    @Nonnull
    public static PerspectivesManagerPlace get(ProjectId projectId) {
        return new AutoValue_PerspectivesManagerPlace(projectId, null);
    }

    @Nonnull
    public static PerspectivesManagerPlace get(ProjectId projectId, Place nextPlace) {
        return new AutoValue_PerspectivesManagerPlace(projectId, nextPlace);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nullable
    protected abstract Place getNextPlaceInternal();

    @Nonnull
    public Optional<Place> getNextPlace() {
        return Optional.ofNullable(getNextPlaceInternal());
    }
}

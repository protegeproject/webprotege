package edu.stanford.bmir.protege.web.client.form;

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
 * 2019-11-17
 */
@AutoValue
public abstract class FormsPlace extends Place implements HasProjectId {

    public static FormsPlace get(@Nonnull ProjectId projectId,
                                 @Nonnull Optional<Place> nextPlace) {
        return new AutoValue_FormsPlace(projectId, nextPlace.orElse(null));
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nullable
    protected abstract Place getNextPlaceInternal();

    @Nonnull
    public Optional<Place> getNextPlace() {
        return Optional.ofNullable(getNextPlaceInternal());
    }
}

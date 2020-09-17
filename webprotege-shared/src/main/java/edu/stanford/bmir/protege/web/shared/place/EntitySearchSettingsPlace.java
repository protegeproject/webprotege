package edu.stanford.bmir.protege.web.shared.place;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
@AutoValue
public abstract class EntitySearchSettingsPlace extends Place implements HasProjectId {

    public static EntitySearchSettingsPlace get(@Nonnull ProjectId projectId,
                                                @Nonnull Optional<Place> nextPlace) {
        return new AutoValue_EntitySearchSettingsPlace(projectId, nextPlace);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract Optional<Place> getNextPlace();
}

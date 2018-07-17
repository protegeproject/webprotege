package edu.stanford.bmir.protege.web.shared.place;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
public abstract class LanguageSettingsPlace extends Place {

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract Optional<Place> getNextPlace();

    public static LanguageSettingsPlace get(@Nonnull ProjectId projectId,
                                            @Nonnull Place nextPlace) {
        return new AutoValue_LanguageSettingsPlace(projectId, Optional.of(nextPlace));
    }

    public static LanguageSettingsPlace get(@Nonnull ProjectId projectId) {
        return new AutoValue_LanguageSettingsPlace(projectId, Optional.empty());
    }
}

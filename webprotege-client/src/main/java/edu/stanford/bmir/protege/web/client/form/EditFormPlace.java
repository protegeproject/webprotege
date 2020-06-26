package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
@AutoValue
public abstract class EditFormPlace extends Place implements HasProjectId {

    public static EditFormPlace get(@Nonnull ProjectId projectId,
                                    @Nonnull FormId formId,
                                    @Nullable Place nextPlace) {
        return new AutoValue_EditFormPlace(projectId, formId, nextPlace);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();

    @Nullable
    protected abstract Place getNextPlaceInternal();

    @Nonnull
    public Optional<Place> getNextPlace() {
        return Optional.ofNullable(getNextPlaceInternal());
    }

}

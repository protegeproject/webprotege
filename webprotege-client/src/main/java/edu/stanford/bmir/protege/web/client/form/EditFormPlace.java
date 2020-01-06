package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
@AutoValue
public abstract class EditFormPlace extends Place implements HasProjectId {

    public static EditFormPlace get(@Nonnull ProjectId projectId,
                                    @Nonnull FormId formId) {
        return new AutoValue_EditFormPlace(projectId, formId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}

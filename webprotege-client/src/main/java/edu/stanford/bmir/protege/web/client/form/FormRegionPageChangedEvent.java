package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 */
@AutoValue
public abstract class FormRegionPageChangedEvent {

    interface FormRegionPageChangedHandler {
        void handleFormRegionPageChanged(@Nonnull FormRegionPageChangedEvent event);
    }

    @Nonnull
    public static FormRegionPageChangedEvent get(@Nonnull FormId formId) {
        return new AutoValue_FormRegionPageChangedEvent(formId);
    }

    @Nonnull
    public abstract FormId getFormId();

}

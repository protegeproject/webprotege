package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormsResult implements Result {

    private ImmutableList<FormData> formData;

    public GetEntityFormsResult(@Nonnull ImmutableList<FormData> formData) {
        this.formData = formData;
    }

    private GetEntityFormsResult() {
    }

    @Nonnull
    public ImmutableList<FormData> getFormData() {
        return formData;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetEntityFormsResult")
                .addValue(formData)
                .toString();
    }
}

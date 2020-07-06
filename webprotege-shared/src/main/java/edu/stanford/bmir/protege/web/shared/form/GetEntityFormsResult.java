package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormsResult implements Result {

    private ImmutableList<FormId> filteredFormIds;

    private ImmutableList<FormDataDto> formData;

    public GetEntityFormsResult(@Nonnull ImmutableList<FormId> filteredFormIds,
                                @Nonnull ImmutableList<FormDataDto> formData) {
        this.filteredFormIds = checkNotNull(filteredFormIds);
        this.formData = checkNotNull(formData);
    }

    private GetEntityFormsResult() {
    }

    @Nonnull
    public ImmutableList<FormId> getFilteredFormIds() {
        return filteredFormIds;
    }

    @Nonnull
    public ImmutableList<FormDataDto> getFormData() {
        return formData;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetEntityFormsResult")
                .addValue(formData)
                .toString();
    }
}

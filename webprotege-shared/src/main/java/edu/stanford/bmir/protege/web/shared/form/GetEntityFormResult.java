package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormResult implements Result {

    @Nullable
    private FormDescriptor formDescriptor;

    @Nonnull
    private FormData formData;

    public GetEntityFormResult(@Nonnull FormDescriptor formDescriptor, @Nonnull FormData formData) {
        this.formDescriptor = formDescriptor;
        this.formData = formData;
    }

    public GetEntityFormResult() {
        this.formDescriptor = null;
        this.formData = FormData.empty();
    }

    @Nonnull
    public Optional<FormDescriptor> getFormDescriptor() {
        return Optional.ofNullable(formDescriptor);
    }

    @Nonnull
    public FormData getFormData() {
        return formData;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetEntityFormResult")
                .addValue(formDescriptor)
                .addValue(formData)
                .toString();
    }
}

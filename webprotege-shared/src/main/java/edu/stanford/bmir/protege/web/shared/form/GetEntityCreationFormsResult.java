package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetEntityCreationFormsResult implements Result {

    @Nonnull
    public static GetEntityCreationFormsResult get(@Nonnull ImmutableList<FormDescriptorDto> formDtos) {
        return new AutoValue_GetEntityCreationFormsResult(formDtos);
    }

    @Nonnull
    public abstract ImmutableList<FormDescriptorDto> getFormDescriptors();


}

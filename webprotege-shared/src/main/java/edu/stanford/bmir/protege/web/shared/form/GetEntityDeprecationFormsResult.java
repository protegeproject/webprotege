package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetEntityDeprecationFormsResult implements Result {

    @Nonnull
    public static GetEntityDeprecationFormsResult get(@Nonnull ImmutableList<FormDescriptorDto> formDtos,
                                                      long referencesCount) {
        return new AutoValue_GetEntityDeprecationFormsResult(formDtos, referencesCount);
    }

    @Nonnull
    public abstract ImmutableList<FormDescriptorDto> getFormDescriptors();

    public abstract long getReferencesCount();
}

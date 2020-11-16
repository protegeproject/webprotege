package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

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
                                                      long referencesCount,
                                                      @Nullable CompositeRootCriteria replacementEntityCriteria) {
        return new AutoValue_GetEntityDeprecationFormsResult(formDtos, referencesCount, replacementEntityCriteria);
    }

    @Nonnull
    public abstract ImmutableList<FormDescriptorDto> getFormDescriptors();

    public abstract long getReferencesCount();

    @Nonnull
    public Optional<CompositeRootCriteria> getReplacedByFilterCriteria() {
        return Optional.ofNullable(getReplacedByFilterCriteriaInternal());
    }

    @Nullable
    public abstract CompositeRootCriteria getReplacedByFilterCriteriaInternal();
}

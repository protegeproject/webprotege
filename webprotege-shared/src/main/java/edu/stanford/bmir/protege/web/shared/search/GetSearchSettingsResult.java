package edu.stanford.bmir.protege.web.shared.search;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetSearchSettingsResult implements Result {

    @Nonnull
    public static GetSearchSettingsResult get(@Nonnull ImmutableList<EntitySearchFilter> filters) {
        return new AutoValue_GetSearchSettingsResult(filters);
    }

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getFilters();
}

package edu.stanford.bmir.protege.web.shared.search;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PerformEntitySearchResult implements Result {

    @Nonnull
    public static PerformEntitySearchResult get(String searchString, Page<EntitySearchResult> page) {
        return new AutoValue_PerformEntitySearchResult(searchString, page);
    }

    @Nonnull
    public abstract String getSearchString();

    @Nonnull
    public abstract Page<EntitySearchResult> getResults();
}

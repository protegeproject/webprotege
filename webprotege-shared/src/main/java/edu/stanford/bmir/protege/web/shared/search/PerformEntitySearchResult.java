package edu.stanford.bmir.protege.web.shared.search;

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
public class PerformEntitySearchResult implements Result {

    private int totalResultCount;

    private Page<EntitySearchResult> results;

    @GwtSerializationConstructor
    private PerformEntitySearchResult() {
    }

    public PerformEntitySearchResult(int totalResultCount,
                                     @Nonnull Page<EntitySearchResult> results) {
        this.totalResultCount = totalResultCount;
        this.results = results;
    }

    public int getTotalResultCount() {
        return totalResultCount;
    }

    @Nonnull
    public Page<EntitySearchResult> getResults() {
        return results;
    }

    @Override
    public int hashCode() {
        return results.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PerformEntitySearchResult)) {
            return false;
        }
        PerformEntitySearchResult other = (PerformEntitySearchResult) obj;
        return this.results.equals(other.results);
    }


    @Override
    public String toString() {
        return toStringHelper("PerformEntitySearchResult")
                .add("results", results.getTotalElements())
                .toString();
    }
}

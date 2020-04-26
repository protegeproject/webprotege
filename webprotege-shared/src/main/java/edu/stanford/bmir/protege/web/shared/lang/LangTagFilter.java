package edu.stanford.bmir.protege.web.shared.lang;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class LangTagFilter {

    @Nonnull
    public static LangTagFilter get(@Nonnull ImmutableSet<LangTag> filteredLangTags) {
        return new AutoValue_LangTagFilter(filteredLangTags);
    }

    @Nonnull
    public abstract ImmutableSet<LangTag> getFilteringTags();

    /**
     * Determines if the specified langtag is included by this filter.
     * @param langTag The lang tag.
     */
    public boolean isIncluded(@Nonnull LangTag langTag) {
        ImmutableSet<LangTag> filteringTags = getFilteringTags();
        return filteringTags.isEmpty() || filteringTags.contains(langTag);
    }

}

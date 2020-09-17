package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
@AutoValue
public abstract class EntitySearchFilterMatcher {

    @Nonnull
    public static EntitySearchFilterMatcher get(@Nonnull EntitySearchFilter searchFilter,
                                                @Nonnull Matcher<OWLEntity> matcher) {
        return new AutoValue_EntitySearchFilterMatcher(searchFilter, matcher);
    }

    @Nonnull
    public abstract EntitySearchFilter getFilter();

    @Nonnull
    public abstract Matcher<OWLEntity> getMatcher();
}

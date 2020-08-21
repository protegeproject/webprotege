package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.EntityMatcherFactory;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.server.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public class SearchFiltersDocumentAugmenter implements EntityDocumentAugmenter {

    private ImmutableList<EntitySearchFilterMatcher> searchFilterMatchers;

    @Inject
    public SearchFiltersDocumentAugmenter(@Nonnull ImmutableList<EntitySearchFilterMatcher> searchFilterMatchers) {
        this.searchFilterMatchers = checkNotNull(searchFilterMatchers);
    }


    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        if(searchFilterMatchers.isEmpty()) {
            return;
        }
        for(var searchFilterMatcher : searchFilterMatchers) {
            var searchFilter = searchFilterMatcher.getFilter();
            var matcher = searchFilterMatcher.getMatcher();
            if(matcher.matches(entity)) {
                var filterIdString = searchFilter.getId().getId();
                var field = toDocumentField(filterIdString);
                document.add(field);
            }
        }
    }

    private StringField toDocumentField(String filterId) {
        return new StringField(EntityDocumentFieldNames.SEARCH_FILTER_MATCHES,
                               filterId,
                               Field.Store.NO);
    }
}

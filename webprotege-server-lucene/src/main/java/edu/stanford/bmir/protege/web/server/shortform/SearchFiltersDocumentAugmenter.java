package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.EntityMatcherFactory;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.server.repository.ProjectSearchFiltersRepository;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsNotDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilterId;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public class SearchFiltersDocumentAugmenter implements EntityDocumentAugmenter {


    @Nonnull
    private final ImmutableList<EntitySearchFilter> searchFilters;

    @Nonnull
    private final EntityMatcherFactory entityMatcherFactory;

    @Nonnull
    private ImmutableList<Matcher<OWLEntity>> matchers = ImmutableList.of();

    @Inject
    public SearchFiltersDocumentAugmenter(@Nonnull ProjectSearchFiltersRepository projectSearchFiltersRepository,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull EntityMatcherFactory entityMatcherFactory) {
        this.entityMatcherFactory = checkNotNull(entityMatcherFactory);
        this.searchFilters = projectSearchFiltersRepository.getSearchFilters(projectId);
    }


    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        if(searchFilters.isEmpty()) {
            return;
        }
        if(matchers.size() != searchFilters.size()) {
            matchers = searchFilters.stream()
                         .map(EntitySearchFilter::getEntityMatchCriteria)
                         .map(entityMatcherFactory::getEntityMatcher)
                         .collect(toImmutableList());
        }
        for(int i = 0; i < searchFilters.size(); i++) {
            var searchFilter = searchFilters.get(i);
            var matcher = matchers.get(i);
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

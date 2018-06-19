package edu.stanford.bmir.protege.web.server.tag;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
@ProjectSingleton
public class CriteriaBasedTagsManager {

    @Nonnull
    private final Map<Criteria, TagId> criteria2TagMap = new ConcurrentHashMap<>();

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public CriteriaBasedTagsManager(@Nonnull MatchingEngine matchingEngine) {
        this.matchingEngine = matchingEngine;
    }

    public void putCriteria(@Nonnull Criteria criteria,
                            @Nonnull TagId tagId) {
        criteria2TagMap.put(checkNotNull(criteria), checkNotNull(tagId));
    }

    public Stream<TagId> getTagsForEntity(@Nonnull OWLEntity entity) {
        return criteria2TagMap.entrySet().stream()
                              .map(entry -> {
                                  Criteria criteria = entry.getKey();
                                  TagId tagId = entry.getValue();
                                  boolean matches = matchingEngine.matches(entity, criteria);
                                  if (matches) {
                                      return tagId;
                                  }
                                  else {
                                      return null;
                                  }
                              }).filter(Objects::nonNull);
    }

    public Stream<OWLEntity> getTaggedEntities(@Nonnull TagId tagId) {
        ImmutableList<Criteria> tagCriteria = criteria2TagMap.entrySet().stream()
                                                                    .map(entry -> {
                                                                        Criteria criteria = entry.getKey();
                                                                        TagId criteriaTagId = entry.getValue();
                                                                        if (criteriaTagId.equals(tagId)) {
                                                                            return criteria;
                                                                        }
                                                                        else {
                                                                            return null;
                                                                        }
                                                                    })
                                                                    .filter(Objects::nonNull)
                                                                    .collect(ImmutableList.toImmutableList());
        return matchingEngine.matchAny(tagCriteria);
    }
}

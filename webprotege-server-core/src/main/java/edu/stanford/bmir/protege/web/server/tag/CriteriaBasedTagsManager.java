package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;
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
    private final TagRepository tagRepository;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public CriteriaBasedTagsManager(@Nonnull TagRepository tagRepository,
                                    @Nonnull MatchingEngine matchingEngine) {
        this.tagRepository = checkNotNull(tagRepository);
        this.matchingEngine = checkNotNull(matchingEngine);
    }

    public Stream<TagId> getTagsForEntity(@Nonnull OWLEntity entity) {
        return tagRepository.findTags()
                     .stream()
                     .map(tag -> {
                         if(matchingEngine.matchesAny(entity, tag.getCriteria())) {
                             return tag;
                         }
                         else {
                             return null;
                         }
                     })
                     .filter(Objects::nonNull)
                     .map(Tag::getTagId);
    }

    public Stream<OWLEntity> getTaggedEntities(@Nonnull TagId tagId) {
        return tagRepository.findTagByTagId(tagId)
                     .map(tag -> matchingEngine.matchAny(tag.getCriteria()))
                     .orElse(Stream.empty());
    }
}

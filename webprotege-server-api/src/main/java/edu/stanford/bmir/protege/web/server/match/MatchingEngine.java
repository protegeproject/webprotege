package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public interface MatchingEngine {

    /**
     * Get the entities that match the specified criteria.
     */
    Stream<OWLEntity> match(@Nonnull Criteria criteria);

    /**
     * Gets the entities that match any of the specified criteria
     */
    Stream<OWLEntity> matchAny(@Nonnull ImmutableList<? extends Criteria> criteria);

    /**
     * Determines whether the specified entity matches the specified criteria
     */
    boolean matches(@Nonnull OWLEntity entity, @Nonnull Criteria criteria);

    boolean matchesAny(@Nonnull OWLEntity entity, @Nonnull ImmutableList<? extends Criteria> criteria);
}

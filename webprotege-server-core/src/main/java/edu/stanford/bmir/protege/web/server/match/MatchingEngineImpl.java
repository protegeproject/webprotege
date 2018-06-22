package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class MatchingEngineImpl implements MatchingEngine {

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public MatchingEngineImpl(@Nonnull @RootOntology OWLOntology rootOntology,
                              @Nonnull MatcherFactory matcherFactory) {
        this.rootOntology = checkNotNull(rootOntology);
        this.matcherFactory = checkNotNull(matcherFactory);
    }

    @Override
    public Stream<OWLEntity> match(@Nonnull Criteria criteria) {
        Matcher<OWLEntity> matcher = getMatcher(criteria);
        return rootOntology.getSignature(Imports.INCLUDED)
                           .stream()
                           .filter(matcher::matches);
    }

    @Override
    public Stream<OWLEntity> matchAny(@Nonnull ImmutableList<? extends Criteria> criteria) {
        ImmutableList<Matcher<OWLEntity>> matchers = criteria.stream()
                                                             .map(this::getMatcher)
                                                             .collect(toImmutableList());
        OrMatcher<OWLEntity> orMatcher = new OrMatcher<>(matchers);
        return rootOntology.getSignature(Imports.INCLUDED)
                           .stream()
                           .filter(orMatcher::matches);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity, @Nonnull Criteria criteria) {
        return getMatcher(criteria).matches(entity);
    }

    @Override
    public boolean matchesAny(@Nonnull OWLEntity entity, @Nonnull ImmutableList<? extends Criteria> criteria) {
        return criteria.stream()
                       .map(this::getMatcher)
                       .anyMatch(c -> c.matches(entity));
    }

    private Matcher<OWLEntity> getMatcher(@Nonnull Criteria rootCriteria) {
        return matcherFactory.getMatcher((RootCriteria) rootCriteria);
    }
}

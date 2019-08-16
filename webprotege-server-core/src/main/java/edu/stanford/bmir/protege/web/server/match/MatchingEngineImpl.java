package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

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
    private final ProjectSignatureIndex projectSignatureIndex;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public MatchingEngineImpl(@Nonnull ProjectSignatureIndex projectSignatureIndex,
                              @Nonnull MatcherFactory matcherFactory) {
        this.projectSignatureIndex = projectSignatureIndex;
        this.matcherFactory = checkNotNull(matcherFactory);
    }

    @Override
    public Stream<OWLEntity> match(@Nonnull Criteria criteria) {
        Matcher<OWLEntity> matcher = getMatcher(criteria);
        return projectSignatureIndex.getSignature()
                                    .filter(matcher::matches);
    }

    @Override
    public Stream<OWLEntity> matchAny(@Nonnull ImmutableList<? extends Criteria> criteria) {
        ImmutableList<Matcher<OWLEntity>> matchers = criteria.stream()
                                                             .map(this::getMatcher)
                                                             .collect(toImmutableList());
        OrMatcher<OWLEntity> orMatcher = new OrMatcher<>(matchers);
        return projectSignatureIndex.getSignature()
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

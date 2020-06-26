package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.shared.crud.ConditionalIriPrefix;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeHierarchyPositionCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityIriPrefixResolver_TestCase {

    public static final String FALLBACK_IRI_PREFIX = "FallbackIriPrefix";

    private static final String CONDITIONAL_IRI_PREFIX = "ConditionalIriPrefix";

    private EntityIriPrefixResolver resolver;

    @Mock
    private MatcherFactory matcherFactory;

    @Mock
    private RootCriteria criteria;

    @Mock
    private Matcher<OWLEntity> matcher;

    @Mock
    private OWLEntity parentEntity;

    @Mock
    private OWLEntity otherParent;

    @Mock
    private EntityCrudKitPrefixSettings prefixSettings;

    @Mock
    private ConditionalIriPrefix conditionalPrefix;

    @Mock
    private CompositeHierarchyPositionCriteria hierarchyCriteria;

    @Mock
    private EntityIriPrefixCriteriaRewriter entityIriPrefixCriteriaRewriter;

    @Before
    public void setUp() {
        resolver = new EntityIriPrefixResolver(matcherFactory, entityIriPrefixCriteriaRewriter);

        when(entityIriPrefixCriteriaRewriter.rewriteCriteria(hierarchyCriteria))
                .thenReturn(criteria);

        when(prefixSettings.getConditionalIriPrefixes())
                .thenReturn(ImmutableList.of(conditionalPrefix));
        when(prefixSettings.getIRIPrefix()).thenReturn(FALLBACK_IRI_PREFIX);
        when(conditionalPrefix.getCriteria())
                .thenReturn(hierarchyCriteria);
        when(conditionalPrefix.getIriPrefix())
                .thenReturn(CONDITIONAL_IRI_PREFIX);

        when(matcherFactory.getMatcher(criteria))
                .thenReturn(matcher);
    }

    @Test
    public void shouldReturnFallbackPrefixForNonMatch() {
        var resolvedPrefix = resolver.getIriPrefix(prefixSettings, ImmutableList.of(otherParent));
        assertThat(resolvedPrefix, is(FALLBACK_IRI_PREFIX));
    }

    @Test
    public void shouldReturnFallbackPrefixForNonMatchOfEmptyParents() {
        var resolvedPrefix = resolver.getIriPrefix(prefixSettings, ImmutableList.of());
        assertThat(resolvedPrefix, is(FALLBACK_IRI_PREFIX));
    }

    @Test
    public void shouldReturnMatchedPrefixForMatch() {
        when(matcher.matches(parentEntity))
                .thenReturn(true);
        var resolvedPrefix = resolver.getIriPrefix(prefixSettings, ImmutableList.of(parentEntity));
        assertThat(resolvedPrefix, is(CONDITIONAL_IRI_PREFIX));
    }
}

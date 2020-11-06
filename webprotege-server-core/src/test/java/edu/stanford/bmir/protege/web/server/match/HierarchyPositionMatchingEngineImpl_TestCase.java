package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.index.IndividualsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Collections;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HierarchyPositionMatchingEngineImpl_TestCase {

    private HierarchyPositionMatchingEngineImpl matchingEngine;

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    @Mock
    private IndividualsByTypeIndex individualsByTypeIndex;

    @Mock
    private OWLClass clsA, clsB, clsC, clsD, clsE;

    @Mock
    private OWLNamedIndividual indI;

    @Mock
    private ProjectSignatureIndex projectSignatureIndex;

    @Before
    public void setUp() throws Exception {
        matchingEngine = new HierarchyPositionMatchingEngineImpl(classHierarchyProvider,
                                                                 individualsByTypeIndex,
                                                                 projectSignatureIndex);
    }

    @Test
    public void shouldReturnChildClasses() {
        when(classHierarchyProvider.getChildren(any())).thenReturn(Collections.singleton(clsB));
        var result = matchingEngine.getMatchingEntities(SubClassOfCriteria.get(clsA, HierarchyFilterType.DIRECT))
                                   .collect(toImmutableSet());
        assertThat(result, contains(clsB));
    }

    @Test
    public void shouldReturnNoChildClasses() {
        when(classHierarchyProvider.getChildren(any())).thenReturn(Collections.emptySet());
        var result = matchingEngine.getMatchingEntities(SubClassOfCriteria.get(clsA, HierarchyFilterType.DIRECT))
                                   .collect(toImmutableSet());
        assertThat(result, emptyCollectionOf(OWLEntity.class));
    }

    @Test
    public void shouldReturnDescendantClasses() {
        when(classHierarchyProvider.getDescendants(any())).thenReturn(Collections.singleton(clsB));
        var result = matchingEngine.getMatchingEntities(SubClassOfCriteria.get(clsA, HierarchyFilterType.ALL))
                                   .collect(toImmutableSet());
        assertThat(result, contains(clsB));
    }

    @Test
    public void shouldReturnNoDescendantClasses() {
        when(classHierarchyProvider.getDescendants(any())).thenReturn(Collections.emptySet());
        var result = matchingEngine.getMatchingEntities(SubClassOfCriteria.get(clsA, HierarchyFilterType.ALL))
                                   .collect(toImmutableSet());
        assertThat(result, emptyCollectionOf(OWLEntity.class));
    }

    @Test
    public void shouldReturnDirectInstances() {
        when(individualsByTypeIndex.getIndividualsByType(clsA,
                                                         InstanceRetrievalMode.DIRECT_INSTANCES)).thenAnswer((inv) -> Stream
                .of(indI));
        var result = matchingEngine.getMatchingEntities(InstanceOfCriteria.get(clsA, HierarchyFilterType.DIRECT))
                                   .collect(toImmutableSet());
        assertThat(result, contains(indI));
    }

    @Test
    public void shouldReturnIndirectInstances() {
        when(individualsByTypeIndex.getIndividualsByType(clsA,
                                                         InstanceRetrievalMode.ALL_INSTANCES)).thenAnswer((inv) -> Stream
                .of(indI));
        var result = matchingEngine.getMatchingEntities(InstanceOfCriteria.get(clsA, HierarchyFilterType.ALL))
                                   .collect(toImmutableSet());
        assertThat(result, contains(indI));
    }


    @Test
    public void shouldReturnUnion() {
        when(classHierarchyProvider.getChildren(clsA)).thenReturn(Collections.singleton(clsB));
        when(classHierarchyProvider.getChildren(clsC)).thenReturn(Collections.singleton(clsD));
        var result = matchingEngine.getMatchingEntities(CompositeHierarchyPositionCriteria.get(ImmutableList.of(
                SubClassOfCriteria.get(clsA, HierarchyFilterType.DIRECT),
                SubClassOfCriteria.get(clsC, HierarchyFilterType.DIRECT)), MultiMatchType.ANY))
                                   .collect(toImmutableSet());
        assertThat(result, contains(clsB, clsD));
    }

    @Test
    public void shouldReturnIntersection() {
        when(classHierarchyProvider.getChildren(clsA)).thenReturn(ImmutableSet.of(clsB, clsD));
        when(classHierarchyProvider.getChildren(clsC)).thenReturn(Collections.singleton(clsD));
        var result = matchingEngine.getMatchingEntities(CompositeHierarchyPositionCriteria.get(ImmutableList.of(
                SubClassOfCriteria.get(clsA, HierarchyFilterType.DIRECT),
                SubClassOfCriteria.get(clsC, HierarchyFilterType.DIRECT)), MultiMatchType.ALL))
                                   .collect(toImmutableSet());
        assertThat(result, contains(clsD));
    }

    @Test
    public void shouldReturnClassesThatAreNotSubClasses() {
        when(classHierarchyProvider.getChildren(clsA)).thenReturn(ImmutableSet.of(clsB, clsD));
        when(classHierarchyProvider.getDescendants(clsA)).thenReturn(ImmutableSet.of(clsB, clsD));
        when(classHierarchyProvider.getChildren(clsC)).thenReturn(Collections.singleton(clsD));
        when(classHierarchyProvider.getDescendants(clsC)).thenReturn(Collections.singleton(clsD));
        when(projectSignatureIndex.getSignature()).thenAnswer(inv -> Stream.of(clsA, clsB, clsC, clsD));
        var result = matchingEngine.getMatchingEntities(NotSubClassOfCriteria.get(clsC, HierarchyFilterType.ALL))
                                   .collect(toImmutableSet());
        assertThat(result, contains(clsA, clsB, clsC));
    }
}
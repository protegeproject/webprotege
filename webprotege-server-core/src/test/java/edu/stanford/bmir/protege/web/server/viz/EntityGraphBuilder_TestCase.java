package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.viz.Edge;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyAssertionAxiomImpl;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-01
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityGraphBuilder_TestCase {

    private static final int EDGE_LIMIT = 2000;

    private EntityGraphBuilder graphBuilder;

    @Mock
    private RenderingManager renderingManager;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionsIndex;

    @Mock
    private SubClassOfAxiomsBySubClassIndex subClassOfAxiomIndex;

    @Mock
    private ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsIndex;

    @Mock
    private EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Mock
    private OWLOntologyID ontId;

    private EdgeMatcher edgeMatcher = edge -> true;

    @Before
    public void setUp() {
        graphBuilder = new EntityGraphBuilder(renderingManager,
                                              projectOntologiesIndex,
                                              objectPropertyAssertionsIndex,
                                              subClassOfAxiomIndex,
                                              classAssertionAxiomsIndex,
                                              equivalentClassesAxiomsIndex, EDGE_LIMIT, edgeMatcher);
        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(inv -> Stream.of(ontId));
    }

    @Test
    public void shouldAddEdgeForSubClassOfClassName() {
        var subCls = createClass();
        var superCls = createClass();
        var subClassOfAxiom = SubClassOf(subCls, superCls);

        var subClsData = mock(OWLClassData.class);
        var superClsData = mock(OWLClassData.class);

        when(renderingManager.getRendering(subCls))
                .thenReturn(subClsData);

        when(renderingManager.getClassData(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getClassData(superCls))
                .thenReturn(superClsData);

        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(subCls, ontId))
                .thenAnswer(inv -> Stream.of(subClassOfAxiom));

        var graph = graphBuilder.createGraph(subCls);
        var edges = graph.getEdges();

        assertThat(edges, contains(edge(subClsData, superClsData)));
    }


    @Test
    public void shouldAddEdgeForSubClassOfObjectSomeValuesFrom() {
        var subCls = createClass();
        var property = createObjectProperty();
        var fillerCls = createClass();
        var subClassOfAxiom = SubClassOf(subCls, ObjectSomeValuesFrom(property, fillerCls));

        var subClsData = mock(OWLClassData.class);
        var fillerClsData = mock(OWLClassData.class);

        when(renderingManager.getRendering(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getClassData(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getClassData(fillerCls))
                .thenReturn(fillerClsData);
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(mock(OWLObjectPropertyData.class));

        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(subCls, ontId))
                .thenAnswer(inv -> Stream.of(subClassOfAxiom));

        var graph = graphBuilder.createGraph(subCls);
        var edges = graph.getEdges();
        assertThat(edges, contains(edge(subClsData, fillerClsData)));
    }

    @Test
    public void shouldNotAddEdgeForSubClassOfObjectSomeValuesFromWithInverseProperties() {
        var subCls = createClass();
        var property = createObjectProperty();
        var fillerCls = createClass();
        var subClassOfAxiom = SubClassOf(subCls, ObjectSomeValuesFrom(ObjectInverseOf(property), fillerCls));

        var subClsData = mock(OWLClassData.class);

        when(renderingManager.getRendering(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getClassData(subCls))
                .thenReturn(subClsData);
        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(subCls, ontId))
                .thenAnswer(inv -> Stream.of(subClassOfAxiom));

        var graph = graphBuilder.createGraph(subCls);
        var edges = graph.getEdges();
        assertThat(edges.size(), is(0));
    }


    @Test
    public void shouldAddEdgeForSubClassOfObjectHasValue() {
        var subCls = createClass();
        var property = createObjectProperty();
        var fillerInd = createIndividual();
        var objectHasValue = ObjectHasValue(property, fillerInd);
        var subClassOfAxiom = SubClassOf(subCls, objectHasValue);

        var subClsData = mock(OWLClassData.class);
        var fillerIndData = mock(OWLNamedIndividualData.class);

        when(renderingManager.getRendering(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getClassData(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getIndividualData(fillerInd))
                .thenReturn(fillerIndData);
        var propertyData = mock(OWLObjectPropertyData.class);
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(propertyData);

        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(subCls, ontId))
                .thenAnswer(inv -> Stream.of(subClassOfAxiom));

        var graph = graphBuilder.createGraph(subCls);
        var edges = graph.getEdges();
        assertThat(edges, contains(edge(subClsData, fillerIndData)));
    }



    @Test
    public void shouldNotAddEdgeForSubClassOfObjectHasValueWithInverseProperty() {
        var subCls = createClass();
        var property = createObjectProperty();
        var fillerInd = createIndividual();
        var subClassOfAxiom = SubClassOf(subCls, ObjectHasValue(ObjectInverseOf(property), fillerInd));

        var subClsData = mock(OWLClassData.class);

        when(renderingManager.getRendering(subCls))
                .thenReturn(subClsData);
        when(renderingManager.getClassData(subCls))
                .thenReturn(subClsData);

        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(subCls, ontId))
                .thenAnswer(inv -> Stream.of(subClassOfAxiom));

        var graph = graphBuilder.createGraph(subCls);
        var edges = graph.getEdges();
        assertThat(edges.isEmpty(), is(true));
    }


    @Test
    public void shouldAddEdgeForClassAssertionWithClassName() {
        var cls = createClass();
        var ind = createIndividual();
        var clsAssertion = ClassAssertion(cls, ind);
        var indData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getRendering(ind))
                .thenReturn(indData);
        when(renderingManager.getIndividualData(ind))
                .thenReturn(indData);
        var clsData = mock(OWLClassData.class);
        when(renderingManager.getClassData(cls))
                .thenReturn(clsData);

        when(classAssertionAxiomsIndex.getClassAssertionAxioms(ind, ontId))
                .thenAnswer(inv -> Stream.of(clsAssertion));

        var graph = graphBuilder.createGraph(ind);
        var edges = graph.getEdges();
        assertThat(edges, contains(edge(indData, clsData)));
    }

    @Test
    public void shouldAddEdgeForClassAssertionWithObjectSomeValuesFrom() {
        var ind = createIndividual();
        var filler = createClass();
        var property = createObjectProperty();
        var clsAssertion = ClassAssertion(ObjectSomeValuesFrom(property, filler), ind);
        var indData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getRendering(ind))
                .thenReturn(indData);
        when(renderingManager.getIndividualData(ind))
                .thenReturn(indData);
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(mock(OWLObjectPropertyData.class));
        var fillerData = mock(OWLClassData.class);
        when(renderingManager.getClassData(filler))
                .thenReturn(fillerData);

        when(classAssertionAxiomsIndex.getClassAssertionAxioms(ind, ontId))
                .thenAnswer(inv -> Stream.of(clsAssertion));

        var graph = graphBuilder.createGraph(ind);
        var edges = graph.getEdges();
        assertThat(edges, contains(edge(indData, fillerData)));
    }


    @Test
    public void shouldAddEdgeForClassAssertionWithObjectHasValue() {
        var ind = createIndividual();
        var filler = createIndividual();
        var property = createObjectProperty();
        var clsAssertion = ClassAssertion(ObjectHasValue(property, filler), ind);
        var indData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getRendering(ind))
                .thenReturn(indData);
        when(renderingManager.getIndividualData(ind))
                .thenReturn(indData);
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(mock(OWLObjectPropertyData.class));
        var fillerData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getIndividualData(filler))
                .thenReturn(fillerData);

        when(classAssertionAxiomsIndex.getClassAssertionAxioms(ind, ontId))
                .thenAnswer(inv -> Stream.of(clsAssertion));

        var graph = graphBuilder.createGraph(ind);
        var edges = graph.getEdges();
        assertThat(edges, contains(edge(indData, fillerData)));
    }


    @Test
    public void shouldNotAddEdgeForClassAssertionWithObjectSomeValuesFromWithPropertyInverse() {
        var ind = createIndividual();
        var filler = createClass();
        var property = createObjectProperty();
        var clsAssertion = ClassAssertion(ObjectSomeValuesFrom(ObjectInverseOf(property), filler), ind);
        var indData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getRendering(ind))
                .thenReturn(indData);
        when(renderingManager.getIndividualData(ind))
                .thenReturn(indData);
        when(classAssertionAxiomsIndex.getClassAssertionAxioms(ind, ontId))
                .thenAnswer(inv -> Stream.of(clsAssertion));

        var graph = graphBuilder.createGraph(ind);
        var edges = graph.getEdges();
        assertThat(edges.size(), is(0));
    }

    @Test
    public void shouldAddEdgeForObjectPropertyAssertion() {
        var subjectInd = createIndividual();
        var property = createObjectProperty();
        var objectInd = createIndividual();
        var axiom = ObjectPropertyAssertion(property, subjectInd, objectInd);

        var subjectData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getRendering(subjectInd))
                .thenReturn(subjectData);
        when(renderingManager.getIndividualData(subjectInd))
                .thenReturn(subjectData);
        
        var objectData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getIndividualData(objectInd))
                .thenReturn(objectData);

        var propertyData = mock(OWLObjectPropertyData.class);
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(propertyData);

        when(objectPropertyAssertionsIndex.getObjectPropertyAssertions(subjectInd, ontId))
                .thenAnswer(inv -> Stream.of(axiom));

        var graph = graphBuilder.createGraph(subjectInd);
        var edges = graph.getEdges();
        assertThat(edges, contains(edge(subjectData, objectData)));
    }

    @Test
    public void shouldHandleSubClassOfCycles() {
        var clsA = createClass();
        var clsB = createClass();
        var clsASubClassOfClsB = SubClassOf(clsA, clsB);
        var clsBSubClassOfClsA = SubClassOf(clsB, clsA);
        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(clsA, ontId))
                .thenAnswer(inv -> Stream.of(clsASubClassOfClsB));
        when(subClassOfAxiomIndex.getSubClassOfAxiomsForSubClass(clsB, ontId))
                .thenAnswer(inv -> Stream.of(clsBSubClassOfClsA));

        var clsAData = mock(OWLClassData.class);
        when(renderingManager.getRendering(clsA))
                .thenReturn(clsAData);
        when(renderingManager.getClassData(clsA))
                .thenReturn(clsAData);
        var clsBData = mock(OWLClassData.class);
        when(renderingManager.getClassData(clsB))
                .thenReturn(clsBData);

        var graph = graphBuilder.createGraph(clsA);
        var edges = graph.getEdges();
        assertThat(edges, contains(
                edge(clsAData, clsBData),
                edge(clsBData, clsAData)
        ));
    }

    @Test
    public void shouldHandleObjectPropertyAssertionCycles() {
        var indA = createIndividual();
        var indB = createIndividual();
        var property = createObjectProperty();
        var firstAssertion = ObjectPropertyAssertion(property, indA, indB);
        var secondAssertion = ObjectPropertyAssertion(property, indB, indA);
        when(objectPropertyAssertionsIndex.getObjectPropertyAssertions(indA, ontId))
                .thenAnswer(inv -> Stream.of(firstAssertion));
        when(objectPropertyAssertionsIndex.getObjectPropertyAssertions(indB, ontId))
                .thenAnswer(inv -> Stream.of(secondAssertion));

        var indAData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getRendering(indA))
                .thenReturn(indAData);
        when(renderingManager.getIndividualData(indA))
                .thenReturn(indAData);
        var indBData = mock(OWLNamedIndividualData.class);
        when(renderingManager.getIndividualData(indB))
                .thenReturn(indBData);
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(mock(OWLObjectPropertyData.class));

        var graph = graphBuilder.createGraph(indA);
        var edges = graph.getEdges();
        assertThat(edges, contains(
                edge(indAData, indBData),
                edge(indBData, indAData)
        ));
    }

    private OWLNamedIndividual head;

    private OWLNamedIndividual indA;

    @Test
    public void shouldHandleDeepGraph() {
        head = null;
        indA = null;
        OWLObjectProperty property = MockingUtils.mockOWLObjectProperty();
        when(renderingManager.getObjectPropertyData(property))
                .thenReturn(OWLObjectPropertyData.get(property, ImmutableMap.of()));
        // Enough to cause a stack over flow with a recursive implementation of the graph builder
        int count = 1000;
        for(int i = 0; i < count; i++) {
            OWLNamedIndividual indB = MockingUtils.mockOWLNamedIndividual();
            when(renderingManager.getIndividualData(indB))
                    .thenReturn(OWLNamedIndividualData.get(indB, ImmutableMap.of()));
            when(renderingManager.getRendering(indB))
                    .thenReturn(OWLNamedIndividualData.get(indB, ImmutableMap.of()));
            if(head == null) {
                head = indB;
            }
            if(indA != null) {
                when(objectPropertyAssertionsIndex.getObjectPropertyAssertions(indA, ontId))
                        .thenAnswer(answer -> Stream.of(new OWLObjectPropertyAssertionAxiomImpl(
                                indA,
                                property,
                                indB,
                                Collections.emptySet()
                        )));
            }
            indA = indB;
        }
        var graph = graphBuilder.createGraph(head);
        assertThat(graph.getEdgeCount(), is(count - 1));
    }

    private static Matcher<Edge> edge(@Nonnull OWLEntityData tail,
                                      @Nonnull OWLEntityData head) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Edge item) {
                return item.getTail().equals(tail)
                        && item.getHead().equals(head);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Edge(" + tail + " -> " + head + ")");
            }
        };
    }

}

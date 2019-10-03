package edu.stanford.bmir.protege.web.server.viz;

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
import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

    @Before
    public void setUp() {
        graphBuilder = new EntityGraphBuilder(renderingManager,
                                              projectOntologiesIndex,
                                              objectPropertyAssertionsIndex,
                                              subClassOfAxiomIndex,
                                              classAssertionAxiomsIndex,
                                              equivalentClassesAxiomsIndex);
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

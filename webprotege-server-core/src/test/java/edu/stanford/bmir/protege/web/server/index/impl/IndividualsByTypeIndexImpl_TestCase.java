package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureByTypeIndex;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

import java.util.Collections;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@RunWith(MockitoJUnitRunner.class)
public class IndividualsByTypeIndexImpl_TestCase {

    private IndividualsByTypeIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private ProjectSignatureByTypeIndex projectSignatureIndex;

    @Mock
    private ClassAssertionAxiomsByIndividualIndex classAssertionsByIndividual;

    @Mock
    private ClassAssertionAxiomsByClassIndex classAssertionsByClass;

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    @Mock
    private DictionaryManager dictionaryManager;

    @Mock
    private OWLDataFactory dataFactory;

    private OWLClass owlThing = new OWLClassImpl(OWLRDFVocabulary.OWL_THING.getIRI());

    @Mock
    private OWLOntologyID ontologyId;

    private OWLNamedIndividual
            indA = new OWLNamedIndividualImpl(mock(IRI.class)),
            indB = new OWLNamedIndividualImpl(mock(IRI.class));

    @Mock
    private OWLClassAssertionAxiom indATypeClsA;

    private OWLClass
            clsA = new OWLClassImpl(mock(IRI.class)),
            clsB = new OWLClassImpl(mock(IRI.class));


    @Before
    public void setUp() {
        impl = new IndividualsByTypeIndexImpl(projectOntologiesIndex,
                                              projectSignatureIndex,
                                              classAssertionsByIndividual,
                                              classAssertionsByClass,
                                              classHierarchyProvider,
                                              dictionaryManager,
                                              dataFactory);

        // SubClassOf(:clsA, :clsB)
        // ClassAssertion(:clsA :indA)

        when(dataFactory.getOWLThing()).thenReturn(owlThing);

        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(invocation -> Stream.of(ontologyId));

        when(projectSignatureIndex.getSignature(EntityType.NAMED_INDIVIDUAL))
                .thenAnswer(invocation -> Stream.of(indA, indB));

        when(indATypeClsA.getClassExpression())
                .thenReturn(clsA);
        when(indATypeClsA.getIndividual())
                .thenReturn(indA);

        when(classAssertionsByIndividual.getClassAssertionAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(classAssertionsByIndividual.getClassAssertionAxioms(indA, ontologyId))
                .thenAnswer(invocation -> Stream.of(indATypeClsA));

        when(classAssertionsByClass.getClassAssertionAxioms(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(classAssertionsByClass.getClassAssertionAxioms(clsA, ontologyId))
                .thenAnswer(invocation -> Stream.of(indATypeClsA));



        when(classHierarchyProvider.getDescendants(clsB))
                .thenAnswer(invocation -> Collections.singleton(clsA));

        when(dictionaryManager.getShortForm(indA))
                .thenReturn("indA");
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(projectOntologiesIndex,
                                                              projectSignatureIndex,
                                                              classAssertionsByIndividual,
                                                              classAssertionsByClass));
    }

    @Test
    public void shouldGetUntypedIndividualsAsDirectInstancesOfOwlThing() {
        var inds = impl.getIndividualsByType(owlThing, InstanceRetrievalMode.DIRECT_INSTANCES).collect(toSet());
        assertThat(inds, contains(indB));
    }

    @Test
    public void shouldAllIndividualsAsIndirectInstancesOfOwlThing() {
        var inds = impl.getIndividualsByType(owlThing, InstanceRetrievalMode.ALL_INSTANCES).collect(toSet());
        assertThat(inds, containsInAnyOrder(indA, indB));
    }

    @Test
    public void shouldGetDirectAssertedInstancesOfClsA() {
        var inds = impl.getIndividualsByType(clsA, InstanceRetrievalMode.DIRECT_INSTANCES).collect(toSet());
        assertThat(inds, containsInAnyOrder(indA));
    }

    @Test
    public void shouldGetInirectAssertedInstancesOfClsA() {
        var inds = impl.getIndividualsByType(clsA, InstanceRetrievalMode.ALL_INSTANCES).collect(toSet());
        assertThat(inds, containsInAnyOrder(indA));
    }

    @Test
    public void shouldGetDirectAssertedInstancesOfClsB() {
        var inds = impl.getIndividualsByType(clsB, InstanceRetrievalMode.DIRECT_INSTANCES).collect(toSet());
        assertThat(inds.isEmpty(), is(true));
    }

    @Test
    public void shouldGetIndirectAssertedInstancesOfClsB() {
        var inds = impl.getIndividualsByType(clsB, InstanceRetrievalMode.ALL_INSTANCES).collect(toSet());
        assertThat(inds, contains(indA));
    }
}

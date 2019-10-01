package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologySignatureByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.impl.AxiomsByEntityReferenceIndexImpl;
import edu.stanford.bmir.protege.web.server.index.impl.ProjectSignatureByTypeIndexImpl;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectSignatureByTypeIndexImpl_TestCase {

    private ProjectSignatureByTypeIndexImpl impl;


    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLDatatype datatype;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceImpl;

    @Before
    public void setUp() {
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.CLASS))
                .thenAnswer(invocation -> Stream.of(cls));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.OBJECT_PROPERTY))
                .thenAnswer(invocation -> Stream.of(objectProperty));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.DATA_PROPERTY))
                .thenAnswer(invocation -> Stream.of(dataProperty));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.ANNOTATION_PROPERTY))
                .thenAnswer(invocation -> Stream.of(annotationProperty));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.DATATYPE))
                .thenAnswer(invocation -> Stream.of(datatype));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.NAMED_INDIVIDUAL))
                .thenAnswer(invocation -> Stream.of(individual));
        impl = new ProjectSignatureByTypeIndexImpl(axiomsByEntityReferenceImpl);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByEntityReferenceImpl));
    }

    @Test
    public void shouldGetClassesInSignature() {
        var signature = impl.getSignature(EntityType.CLASS).collect(toSet());
        assertThat(signature, hasItem(cls));
    }

    @Test
    public void shouldGetDatatypesInSignature() {
        var signature = impl.getSignature(EntityType.DATATYPE).collect(toSet());
        assertThat(signature, hasItem(datatype));
    }

    @Test
    public void shouldGetObjectPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.OBJECT_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(objectProperty));
    }

    @Test
    public void shouldGetDataPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.DATA_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.ANNOTATION_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(annotationProperty));
    }

    @Test
    public void shouldGetIndividualsPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.NAMED_INDIVIDUAL).collect(toSet());
        assertThat(signature, hasItem(individual));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getSignature(null);
    }
}

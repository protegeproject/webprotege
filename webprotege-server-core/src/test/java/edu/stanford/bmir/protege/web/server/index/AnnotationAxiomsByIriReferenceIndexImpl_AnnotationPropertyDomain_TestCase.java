package edu.stanford.bmir.protege.web.server.index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyDomainAxiomImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationAxiomsByIriReferenceIndexImpl_AnnotationPropertyDomain_TestCase {

    private AnnotationAxiomsByIriReferenceIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private IRI domainIri, otherDomainIri;

    @Mock
    private OWLAnnotation axiomAnnotation;

    @Mock
    private IRI axiomAnnotationValue;

    @Mock
    private OWLAnnotationProperty property;

    private OWLAnnotationPropertyDomainAxiom annotationPropertyDomainAxiom, otherAnnotationPropertyDomainAxiom;


    @Before
    public void setUp() {
        when(projectOntologiesIndex.getOntologyIds()).thenReturn(Stream.of(ontologyId));

        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_ASSERTION, ontologyId))
                .thenReturn(Stream.empty());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_RANGE, ontologyId))
                .thenReturn(Stream.empty());


        annotationPropertyDomainAxiom = new OWLAnnotationPropertyDomainAxiomImpl(property, domainIri, axiomAnnotations());
        otherAnnotationPropertyDomainAxiom = new OWLAnnotationPropertyDomainAxiomImpl(property, otherDomainIri, axiomAnnotations());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_DOMAIN, ontologyId)).thenReturn(Stream.of(annotationPropertyDomainAxiom));

        impl = new AnnotationAxiomsByIriReferenceIndexImpl();
        impl.load(Stream.of(ontologyId), axiomsByTypeIndex);
    }

    private Set<OWLAnnotation> axiomAnnotations() {
        when(axiomAnnotation.getValue())
                .thenReturn(axiomAnnotationValue);
        return Collections.singleton(axiomAnnotation);
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomByDomainIri() {
        var axioms = impl.getReferencingAxioms(domainIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDomainAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomByAxiomAnnotationValue() {
        var axioms = impl.getReferencingAxioms(axiomAnnotationValue, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDomainAxiom));
    }


    @Test
    public void shouldHandleAddAnnotationPropertyDomainAxiom() {
        var addAxiomData = new AddAxiomData(otherAnnotationPropertyDomainAxiom);
        var changeRecord = new OWLOntologyChangeRecord(ontologyId, addAxiomData);

        impl.handleOntologyChanges(Stream.of(changeRecord));

        var axioms = impl.getReferencingAxioms(otherDomainIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItems(otherAnnotationPropertyDomainAxiom));
    }

    @Test
    public void shouldHandleRemoveAnnotationPropertyDomainAxiom() {
        var removeAxiomData = new RemoveAxiomData(annotationPropertyDomainAxiom);
        var changeRecord = new OWLOntologyChangeRecord(ontologyId, removeAxiomData);

        impl.handleOntologyChanges(Stream.of(changeRecord));

        var axioms = impl.getReferencingAxioms(domainIri, ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }
}

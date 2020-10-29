package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyDomainAxiomImpl;

import java.util.Collections;
import java.util.Set;

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
    private OntologyDocumentId ontologyDocumentId;

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
        annotationPropertyDomainAxiom = new OWLAnnotationPropertyDomainAxiomImpl(property, domainIri, axiomAnnotations());
        otherAnnotationPropertyDomainAxiom = new OWLAnnotationPropertyDomainAxiomImpl(property, otherDomainIri, axiomAnnotations());
        impl = new AnnotationAxiomsByIriReferenceIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyDocumentId, annotationPropertyDomainAxiom)));
    }

    private Set<OWLAnnotation> axiomAnnotations() {
        when(axiomAnnotation.getValue())
                .thenReturn(axiomAnnotationValue);
        return Collections.singleton(axiomAnnotation);
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomByDomainIri() {
        var axioms = impl.getReferencingAxioms(domainIri, ontologyDocumentId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDomainAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomByAxiomAnnotationValue() {
        var axioms = impl.getReferencingAxioms(axiomAnnotationValue, ontologyDocumentId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyDomainAxiom));
    }


    @Test
    public void shouldHandleAddAnnotationPropertyDomainAxiom() {
        var changeRecord = AddAxiomChange.of(ontologyDocumentId, otherAnnotationPropertyDomainAxiom);

        impl.applyChanges(ImmutableList.of(changeRecord));

        var axioms = impl.getReferencingAxioms(otherDomainIri, ontologyDocumentId).collect(toSet());
        assertThat(axioms, hasItems(otherAnnotationPropertyDomainAxiom));
    }

    @Test
    public void shouldHandleRemoveAnnotationPropertyDomainAxiom() {
        var changeRecord = RemoveAxiomChange.of(ontologyDocumentId, annotationPropertyDomainAxiom);

        impl.applyChanges(ImmutableList.of(changeRecord));

        var axioms = impl.getReferencingAxioms(domainIri, ontologyDocumentId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }
}

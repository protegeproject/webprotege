package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationAssertionAxiomImpl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationAxiomsByIriReferenceIndexImpl_AnnotationAssertionAxiom_TestCase {

    private AnnotationAxiomsByIriReferenceIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLAnnotationAssertionAxiom annotationAssertionAxiom, otherAnnotationAssertionAxiom;

    @Mock
    private IRI subjectIri, otherSubjectIri;

    @Mock
    private IRI valueIri, otherValueIri;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLAnnotation axiomAnnotation;

    @Mock
    private IRI axiomAnnotationValue;


    @Before
    public void setUp() {
        // Use real objects rather than mocks because we need proper visitor functionality
        annotationAssertionAxiom = new OWLAnnotationAssertionAxiomImpl(subjectIri,
                                                                       property,
                                                                       valueIri,
                                                                       axiomAnnotations());

        otherAnnotationAssertionAxiom = new OWLAnnotationAssertionAxiomImpl(otherSubjectIri,
                                                                            property,
                                                                            otherValueIri,
                                                                            axiomAnnotations());

        impl = new AnnotationAxiomsByIriReferenceIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, annotationAssertionAxiom)));
    }

    private Set<OWLAnnotation> axiomAnnotations() {
        when(axiomAnnotation.getValue())
                .thenReturn(axiomAnnotationValue);
        return Collections.singleton(axiomAnnotation);
    }

    @Test
    public void shouldGetAnnotationPropertyAssertionAxiomBySubject() {
        var axioms = impl.getReferencingAxioms(subjectIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationAssertionAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyAssertionAxiomByValue() {
        var axioms = impl.getReferencingAxioms(valueIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationAssertionAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyAssertionAxiomByAxiomAnnotationValue() {
        var axioms = impl.getReferencingAxioms(axiomAnnotationValue, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationAssertionAxiom));
    }


    @Test
    public void shouldHandleAddAnnotationAssertionAxiom() {
        var addAxiom = AddAxiomChange.of(ontologyId, otherAnnotationAssertionAxiom);
        impl.applyChanges(ImmutableList.of(addAxiom));

        var axiomsBySubjectIri = impl.getReferencingAxioms(otherSubjectIri, ontologyId).collect(toSet());
        assertThat(axiomsBySubjectIri, hasItems(otherAnnotationAssertionAxiom));

        var axiomsByValueIri = impl.getReferencingAxioms(otherValueIri, ontologyId).collect(toSet());
        assertThat(axiomsByValueIri, hasItems(otherAnnotationAssertionAxiom));
    }

    @Test
    public void shouldHandleRemoveAnnotationAssertionAxiom() {
        var removeAxiom = RemoveAxiomChange.of(ontologyId, annotationAssertionAxiom);
        impl.applyChanges(ImmutableList.of(removeAxiom));

        var axiomsBySubjectIri = impl.getReferencingAxioms(subjectIri, ontologyId).collect(toSet());
        assertThat(axiomsBySubjectIri.isEmpty(), is(true));

        var axiomsByValueIri = impl.getReferencingAxioms(valueIri, ontologyId).collect(toSet());
        assertThat(axiomsByValueIri.isEmpty(), Matchers.is(true));
    }
}

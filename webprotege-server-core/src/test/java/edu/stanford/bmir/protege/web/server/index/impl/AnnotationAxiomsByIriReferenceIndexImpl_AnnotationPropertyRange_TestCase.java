package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.impl.AnnotationAxiomsByIriReferenceIndexImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyRangeAxiomImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

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
public class AnnotationAxiomsByIriReferenceIndexImpl_AnnotationPropertyRange_TestCase {

    private AnnotationAxiomsByIriReferenceIndexImpl impl;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private IRI rangeIri, otherRangeIri;

    private OWLAnnotationPropertyRangeAxiom annotationPropertyRangeAxiom, otherAnnotationPropertyRangeAxiom;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLAnnotation axiomAnnotation;

    @Mock
    private IRI axiomAnnotationValue;

    @Before
    public void setUp() {

        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_ASSERTION, ontologyId))
                .thenReturn(Stream.empty());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_DOMAIN, ontologyId))
                .thenReturn(Stream.empty());


        annotationPropertyRangeAxiom = new OWLAnnotationPropertyRangeAxiomImpl(property, rangeIri, axiomAnnotations());
        otherAnnotationPropertyRangeAxiom = new OWLAnnotationPropertyRangeAxiomImpl(property, otherRangeIri, axiomAnnotations());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_RANGE, ontologyId)).thenReturn(Stream.of(annotationPropertyRangeAxiom));

        impl = new AnnotationAxiomsByIriReferenceIndexImpl();
        impl.load(Stream.of(ontologyId), axiomsByTypeIndex);
    }

    private Set<OWLAnnotation> axiomAnnotations() {
        when(axiomAnnotation.getValue())
                .thenReturn(axiomAnnotationValue);
        return Collections.singleton(axiomAnnotation);
    }

    @Test
    public void shouldGetAnnotationPropertyRangeAxiomByRangeIri() {
        var axioms = impl.getReferencingAxioms(rangeIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyRangeAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyRangeAxiomByAxiomAnnotationValue() {
        var axioms = impl.getReferencingAxioms(axiomAnnotationValue, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyRangeAxiom));
    }

    @Test
    public void shouldHandleAddAnnotationPropertyRangeAxiom() {
        var addAxiom = AddAxiomChange.of(ontologyId, otherAnnotationPropertyRangeAxiom);
        impl.handleOntologyChanges(ImmutableList.of(addAxiom));

        var axioms = impl.getReferencingAxioms(otherRangeIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItems(otherAnnotationPropertyRangeAxiom));
    }

    @Test
    public void shouldHandleRemoveAnnotationPropertyRangeAxiom() {
        var removeAxiom = RemoveAxiomChange.of(ontologyId, annotationPropertyRangeAxiom);
        impl.handleOntologyChanges(ImmutableList.of(removeAxiom));

        var axioms = impl.getReferencingAxioms(rangeIri, ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }
}

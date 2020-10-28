package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

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
 * 2019-08-13
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationPropertyRangeAxiomsIndexImpl_TestCase {

    private AnnotationPropertyRangeAxiomsIndexImpl impl;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLAnnotationPropertyRangeAxiom axiom;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Before
    public void setUp() {
        when(axiom.getProperty())
                .thenReturn(property);
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_RANGE, ontologyDocumentId))
                .thenAnswer(invocation -> Stream.of(axiom));
        impl = new AnnotationPropertyRangeAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldGetAnnotationPropertyRangeAxiomForProperty() {
        var axioms = impl.getAnnotationPropertyRangeAxioms(property, ontologyDocumentId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getAnnotationPropertyRangeAxioms(property, mock(OntologyDocumentId.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getAnnotationPropertyRangeAxioms(mock(OWLAnnotationProperty.class), ontologyDocumentId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getAnnotationPropertyRangeAxioms(property, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullProperty() {
        impl.getAnnotationPropertyRangeAxioms(null, ontologyDocumentId);
    }

}

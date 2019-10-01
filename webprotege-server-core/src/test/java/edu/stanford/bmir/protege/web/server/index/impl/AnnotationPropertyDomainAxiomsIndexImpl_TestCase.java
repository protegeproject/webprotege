package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
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
public class AnnotationPropertyDomainAxiomsIndexImpl_TestCase {

    private AnnotationPropertyDomainAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLAnnotationPropertyDomainAxiom axiom;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Before
    public void setUp() {
        when(axiom.getProperty())
                .thenReturn(property);
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_DOMAIN, ontologyID))
                .thenAnswer(invocation -> Stream.of(axiom));
        impl = new AnnotationPropertyDomainAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldGetAnnotationPropertyDomainAxiomForProperty() {
        var axioms = impl.getAnnotationPropertyDomainAxioms(property, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getAnnotationPropertyDomainAxioms(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getAnnotationPropertyDomainAxioms(mock(OWLAnnotationProperty.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getAnnotationPropertyDomainAxioms(property, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullProperty() {
        impl.getAnnotationPropertyDomainAxioms(null, ontologyID);
    }

}

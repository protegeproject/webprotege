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
 * 2019-08-10
 */
@RunWith(MockitoJUnitRunner.class)
public class DataPropertyDomainAxiomsIndexImpl_TestCase {

    private DataPropertyDomainAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLDataProperty property;

    @Mock
    private OWLDataPropertyDomainAxiom axiom;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Before
    public void setUp() {
        when(axiom.getProperty())
                .thenReturn(property);
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.of());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.DATA_PROPERTY_DOMAIN, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
        impl = new DataPropertyDomainAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldGetDataPropertyDomainAxiomForProperty() {
        var axioms = impl.getDataPropertyDomainAxioms(property, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getDataPropertyDomainAxioms(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getDataPropertyDomainAxioms(mock(OWLDataProperty.class), ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getDataPropertyDomainAxioms(property, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullProperty() {
        impl.getDataPropertyDomainAxioms(null, ontologyId);
    }
}

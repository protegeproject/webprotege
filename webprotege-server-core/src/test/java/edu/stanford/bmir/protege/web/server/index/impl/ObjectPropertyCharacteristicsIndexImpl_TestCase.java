package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.impl.ObjectPropertyCharacteristicsIndexImpl;
import edu.stanford.bmir.protege.web.server.index.impl.OntologyIndex;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectPropertyCharacteristicsIndexImpl_TestCase {

    private ObjectPropertyCharacteristicsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLObjectProperty property;

    @Before
    public void setUp() {
        impl = new ObjectPropertyCharacteristicsIndexImpl(ontologyIndex);
        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));
    }

    @Test
    public void shouldReturnTrueForFunctional() {
        when(ontology.getFunctionalObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLFunctionalObjectPropertyAxiom.class)));
        assertHasCharacteristic(ObjectPropertyCharacteristic.FUNCTIONAL, true);
    }

    private void assertHasCharacteristic(ObjectPropertyCharacteristic characteristic,
                                         boolean b) {
        var hasCharacteristic = impl.hasCharacteristic(property, characteristic, ontologyId);
        assertThat(hasCharacteristic, is(b));
    }

    @Test
    public void shouldReturnFalseForFunctional() {
        assertHasCharacteristic(ObjectPropertyCharacteristic.FUNCTIONAL, false);
    }

    @Test
    public void shouldReturnTrueForInverseFunctional() {
        when(ontology.getInverseFunctionalObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLInverseFunctionalObjectPropertyAxiom.class)));
        assertHasCharacteristic(ObjectPropertyCharacteristic.INVERSE_FUNCTIONAL, true);
    }

    @Test
    public void shouldReturnFalseForInverseFunctional() {
        assertHasCharacteristic(ObjectPropertyCharacteristic.INVERSE_FUNCTIONAL, false);
    }

    @Test
    public void shouldReturnTrueForSymmetric() {
        when(ontology.getSymmetricObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLSymmetricObjectPropertyAxiom.class)));
        assertHasCharacteristic(ObjectPropertyCharacteristic.SYMMETRIC, true);
    }

    @Test
    public void shouldReturnFalseForSymmetric() {
        assertHasCharacteristic(ObjectPropertyCharacteristic.SYMMETRIC, false);
    }

    @Test
    public void shouldReturnTrueForAsymmetric() {
        when(ontology.getAsymmetricObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLAsymmetricObjectPropertyAxiom.class)));
        assertHasCharacteristic(ASYMMETRIC, true);
    }

    @Test
    public void shouldReturnFalseForAsymmetric() {
        assertHasCharacteristic(ASYMMETRIC, false);
    }

    @Test
    public void shouldReturnTrueForReflexive() {
        when(ontology.getReflexiveObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLReflexiveObjectPropertyAxiom.class)));
        assertHasCharacteristic(REFLEXIVE, true);
    }

    @Test
    public void shouldReturnFalseForReflexive() {
        assertHasCharacteristic(REFLEXIVE, false);
    }

    @Test
    public void shouldReturnTrueForIrreflexive() {
        when(ontology.getIrreflexiveObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLIrreflexiveObjectPropertyAxiom.class)));
        assertHasCharacteristic(IRREFLEXIVE, true);
    }

    @Test
    public void shouldReturnFalseForIrreflexive() {
        assertHasCharacteristic(IRREFLEXIVE, false);
    }

    @Test
    public void shouldReturnTrueForTransitive() {
        when(ontology.getTransitiveObjectPropertyAxioms(property))
                .thenReturn(Collections.singleton(mock(OWLTransitiveObjectPropertyAxiom.class)));
        assertHasCharacteristic(TRANSITIVE, true);
    }

    @Test
    public void shouldReturnFalseForTransitive() {
        assertHasCharacteristic(TRANSITIVE, false);
    }

    @Test
    public void shouldReturnFalseForUnknownOntologyId() {
        assertThat(impl.hasCharacteristic(property, FUNCTIONAL, mock(OWLOntologyID.class)), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownProperty() {
        assertThat(impl.hasCharacteristic(mock(OWLObjectProperty.class), FUNCTIONAL, ontologyId), is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfPropertyIsNull() {
        impl.hasCharacteristic(null, FUNCTIONAL, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfCharacteristicIsNull() {
        impl.hasCharacteristic(property, null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.hasCharacteristic(property, FUNCTIONAL, null);
    }
}


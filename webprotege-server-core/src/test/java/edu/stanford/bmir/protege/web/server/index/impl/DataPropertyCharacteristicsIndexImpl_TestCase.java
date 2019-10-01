package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

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
public class DataPropertyCharacteristicsIndexImpl_TestCase {

    private DataPropertyCharacteristicsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLDataProperty property;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OWLFunctionalDataPropertyAxiom axiom;

    @Before
    public void setUp() {
        when(axiom.getProperty())
                .thenReturn(property);
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        impl = new DataPropertyCharacteristicsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldReturnTrueForFunctional() {
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.FUNCTIONAL_DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
        assertThat(impl.isFunctional(property, ontologyId), is(true));
    }

    @Test
    public void shouldReturnFalseForFunctional() {
        assertThat(impl.isFunctional(property, ontologyId), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownOntologyId() {
        assertThat(impl.isFunctional(property, mock(OWLOntologyID.class)), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownProperty() {
        assertThat(impl.isFunctional(mock(OWLDataProperty.class), ontologyId), is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfPropertyIsNull() {
        impl.isFunctional(null, ontologyId);
    }


    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.isFunctional(property, null);
    }
}

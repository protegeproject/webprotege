package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsByValueIndex;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationAssertionAxiomsByValueIndexImpl_TestCase {

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationAssertionAxiom axiom;

    @Mock
    private IRI iriValue;

    @Mock
    private OWLAnonymousIndividual individualValue;

    @Mock
    private OWLLiteral literalValue;

    private AnnotationAssertionAxiomsByValueIndexImpl index;

    @Before
    public void setUp() throws Exception {
        index = new AnnotationAssertionAxiomsByValueIndexImpl();
    }


    @Test
    public void shouldIndexAxiomByIriValue() {
        when(axiom.getValue()).thenReturn(iriValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(iriValue, ontologyId).collect(toSet()), contains(axiom));
    }

    @Test
    public void shouldIndexAxiomByAnonymousIndividualValue() {
        when(axiom.getValue()).thenReturn(individualValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(individualValue, ontologyId).collect(toSet()), contains(axiom));
    }

    @Test
    public void shouldNotIndexAxiomByLiteralValue() {
        when(axiom.getValue()).thenReturn(literalValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(literalValue, ontologyId).collect(toSet()), not(contains(axiom)));
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAxiomIsNull() {
        index.getAxiomsByValue(null, ontologyId);
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        index.getAxiomsByValue(iriValue, null);
    }
}
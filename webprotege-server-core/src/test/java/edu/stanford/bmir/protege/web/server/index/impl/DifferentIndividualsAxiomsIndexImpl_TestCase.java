package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
@RunWith(MockitoJUnitRunner.class)
public class DifferentIndividualsAxiomsIndexImpl_TestCase {

    private DifferentIndividualsAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLIndividual individual;

    @Mock
    private OWLDifferentIndividualsAxiom axiom;

    @Before
    public void setUp() {
        when(axiom.getIndividuals())
                .thenReturn(Collections.singleton(individual));
        impl = new DifferentIndividualsAxiomsIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyID, axiom)));
    }

    @Test
    public void shouldGetDifferentIndividualsAxiomForIndividual() {
        var axioms = impl.getDifferentIndividualsAxioms(individual, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getDifferentIndividualsAxioms(individual, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownIndividual() {
        var axioms = impl.getDifferentIndividualsAxioms(mock(OWLIndividual.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getDifferentIndividualsAxioms(individual, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullIndividual() {
        impl.getDifferentIndividualsAxioms(null, ontologyID);
    }
}

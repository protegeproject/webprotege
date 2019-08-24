package edu.stanford.bmir.protege.web.server.index;

import com.google.common.collect.ImmutableSet;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAxiomsIndexImpl_TestCase {

    private OntologyAxiomsIndexImpl impl;

    @Mock
    private OntologyIndex ontologyIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLOntology ontology;

    @Mock
    private OWLAxiom axiom;

    @Before
    public void setUp() {
        impl = new OntologyAxiomsIndexImpl(ontologyIndex);

        when(ontologyIndex.getOntology(any()))
                .thenReturn(Optional.empty());
        when(ontologyIndex.getOntology(ontologyId))
                .thenReturn(Optional.of(ontology));

        when(ontology.getAxioms())
                .thenReturn(ImmutableSet.of(axiom));
    }

    @Test
    public void shouldGetAxioms() {
        var axioms = impl.getAxioms(ontologyId).collect(toSet());
        assertThat(axioms, contains(axiom));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntologyId() {
        var axioms = impl.getAxioms(mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.getAxioms(null);
    }
}

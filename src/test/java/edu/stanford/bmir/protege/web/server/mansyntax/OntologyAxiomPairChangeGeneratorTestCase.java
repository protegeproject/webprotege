package edu.stanford.bmir.protege.web.server.mansyntax;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAxiomPairChangeGeneratorTestCase {

    @Mock
    private OWLOntology ont;

    @Mock
    private OWLAxiom ax;

    private OntologyAxiomPair pair;
    private OntologyAxiomPairChangeGenerator generator;

    @Before
    public void setUp() throws Exception {
        pair = mock(OntologyAxiomPair.class);
        when(pair.getOntology()).thenReturn(ont);
        when(pair.getAxiom()).thenReturn(ax);
        generator = new OntologyAxiomPairChangeGenerator();
    }

    @Test
    public void shouldGenerateAddAxiom() {
        Set<OntologyAxiomPair> from = Collections.emptySet();
        Set<OntologyAxiomPair> to = Collections.singleton(pair);
        List<OWLOntologyChange> changes = generator.generateChanges(from, to);
        assertThat(changes, hasSize(1));
        OWLOntologyChange change = changes.get(0);
        assertThat(change.getOntology(), is(equalTo(ont)));
        assertThat(change.getAxiom(), is(equalTo(ax)));
        assertThat(change.isAddAxiom(), is(true));
    }

    @Test
    public void shouldGenerateRemoveAxiom() {
        Set<OntologyAxiomPair> from = Collections.singleton(pair);
        Set<OntologyAxiomPair> to = Collections.emptySet();
        OntologyAxiomPairChangeGenerator generator = new OntologyAxiomPairChangeGenerator();
        List<OWLOntologyChange> changes = generator.generateChanges(from, to);
        assertThat(changes, hasSize(1));
        OWLOntologyChange change = changes.get(0);
        assertThat(change.getOntology(), is(equalTo(ont)));
        assertThat(change.getAxiom(), is(equalTo(ax)));
        assertThat(change.isRemoveAxiom(), is(true));
    }

    @Test
    public void shouldGenerateEmptyList() {
        Set<OntologyAxiomPair> from = Collections.singleton(pair);
        Set<OntologyAxiomPair> to = Collections.singleton(pair);
        OntologyAxiomPairChangeGenerator generator = new OntologyAxiomPairChangeGenerator();
        List<OWLOntologyChange> changes = generator.generateChanges(from, to);
        assertThat(changes, is(empty()));
    }

}

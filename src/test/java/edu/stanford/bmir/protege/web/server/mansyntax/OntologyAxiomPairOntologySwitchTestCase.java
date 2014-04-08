package edu.stanford.bmir.protege.web.server.mansyntax;

import org.coode.owlapi.manchesterowlsyntax.OntologyAxiomPair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAxiomPairOntologySwitchTestCase {

    @Mock
    private OWLOntology ontA;

    @Mock
    private OWLOntology ontB;

    @Mock
    private OWLAxiom ax;

    private OntologyAxiomPair pairA;

    private OntologyAxiomPair pairB;
    private OntologyAxiomPairChangeGenerator generator;

    @Before
    public void setUp() throws Exception {
        pairA = mock(OntologyAxiomPair.class);
        when(pairA.getOntology()).thenReturn(ontA);
        when(pairA.getAxiom()).thenReturn(ax);

        pairB = mock(OntologyAxiomPair.class);
        when(pairB.getOntology()).thenReturn(ontB);
        when(pairB.getAxiom()).thenReturn(ax);
        generator = new OntologyAxiomPairChangeGenerator();
    }


    @Test
    public void shouldGenerateRemoveThenAdd() {
        Set<OntologyAxiomPair> from = Collections.singleton(pairA);
        Set<OntologyAxiomPair> to = Collections.singleton(pairB);
        List<OWLOntologyChange> changes = generator.generateChanges(from, to);
        assertThat(changes, hasSize(2));
        OWLOntologyChange change0 = changes.get(0);
        assertThat(change0.isRemoveAxiom(), is(true));
        assertThat(change0.getAxiom(), is(equalTo(ax)));
        assertThat(change0.getOntology(), is(equalTo(ontA)));


        OWLOntologyChange change1 = changes.get(1);
        assertThat(change1.isAddAxiom(), is(true));
        assertThat(change1.getAxiom(), is(equalTo(ax)));
        assertThat(change1.getOntology(), is(equalTo(ontB)));
    }

}

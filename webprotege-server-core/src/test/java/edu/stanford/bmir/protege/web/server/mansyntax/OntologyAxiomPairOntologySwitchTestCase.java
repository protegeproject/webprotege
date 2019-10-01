package edu.stanford.bmir.protege.web.server.mansyntax;

import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import java.util.Collections;
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
    private OWLOntologyID ontAId;

    @Mock
    private OWLOntology ontB;

    @Mock
    private OWLOntologyID ontBId;

    @Mock
    private OWLAxiom ax;

    private OntologyAxiomPair pairA;

    private OntologyAxiomPair pairB;
    private OntologyAxiomPairChangeGenerator generator;

    @Before
    public void setUp() throws Exception {
        when(ontA.getOntologyID())
                .thenReturn(ontAId);
        when(ontB.getOntologyID())
                .thenReturn(ontBId);
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
        var changes = generator.generateChanges(from, to);
        assertThat(changes, hasSize(2));
        OntologyChange change0 = changes.get(0);
        assertThat(change0.isRemoveAxiom(), is(true));
        assertThat(change0.getAxiomOrThrow(), is(equalTo(ax)));
        assertThat(change0.getOntologyId(), is(equalTo(ontAId)));


        OntologyChange change1 = changes.get(1);
        assertThat(change1.isAddAxiom(), is(true));
        assertThat(change1.getAxiomOrThrow(), is(equalTo(ax)));
        assertThat(change1.getOntologyId(), is(equalTo(ontBId)));
    }

}

package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/03/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ModifiedProjectOntologiesCalculator_TestCase {


    private ModifiedProjectOntologiesCalculator calculator;

    @Mock
    private OWLOntology projectOntology;

    @Mock
    private OWLOntology externalOntology;

    @Mock
    private OntologyDiffCalculator diffCalculator;

    @Mock
    private OntologyDiff ontologyDiff;

    @Before
    public void setUp() throws Exception {
        ImmutableSet<OWLOntology> projectOntologies = ImmutableSet.of(projectOntology);
        ImmutableSet<OWLOntology> externalOntologies = ImmutableSet.of(externalOntology);

        calculator = new ModifiedProjectOntologiesCalculator(projectOntologies, externalOntologies, diffCalculator);

        when(diffCalculator.computeDiff(projectOntology, externalOntology)).thenReturn(ontologyDiff);
    }

    @Test
    public void shouldNotPerformDiffForDifferentOntologyIRIs() {
        OWLOntologyID projectOntologyId = new OWLOntologyID(mock(IRI.class));
        when(projectOntology.getOntologyID()).thenReturn(projectOntologyId);
        OWLOntologyID externalOntologyId = new OWLOntologyID(mock(IRI.class));
        when(externalOntology.getOntologyID()).thenReturn(externalOntologyId);
        assertThat(calculator.getModifiedOntologyDiffs(), is(empty()));
    }

    @Test
    public void shouldPerformDiffForSameOntologyIRIs() {
        IRI commonIRI = mock(IRI.class);
        OWLOntologyID commonOntologyId = new OWLOntologyID(commonIRI);
        when(projectOntology.getOntologyID()).thenReturn(commonOntologyId);
        when(externalOntology.getOntologyID()).thenReturn(commonOntologyId);

        assertThat(calculator.getModifiedOntologyDiffs(), contains(ontologyDiff));
    }

    @Test
    public void shouldIgnoreVersionIRI() {
        IRI commonIRI = mock(IRI.class);
        OWLOntologyID projectOntologyId = new OWLOntologyID(commonIRI, mock(IRI.class));
        when(projectOntology.getOntologyID()).thenReturn(projectOntologyId);
        OWLOntologyID externalOntologyId = new OWLOntologyID(commonIRI, mock(IRI.class));
        when(externalOntology.getOntologyID()).thenReturn(externalOntologyId);

        assertThat(calculator.getModifiedOntologyDiffs(), contains(ontologyDiff));
    }

    @Test
    public void shouldIgnoreAnonymousOntologies() {
        when(projectOntology.getOntologyID()).thenReturn(new OWLOntologyID());
        when(externalOntology.getOntologyID()).thenReturn(new OWLOntologyID());
        assertThat(calculator.getModifiedOntologyDiffs(), is(empty()));
    }
}

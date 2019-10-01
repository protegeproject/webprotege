package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    private Ontology projectOntology;

    @Mock
    private Ontology externalOntology;

    @Mock
    private OntologyDiffCalculator diffCalculator;

    @Mock
    private OntologyDiff ontologyDiff;

    @Before
    public void setUp() throws Exception {
        var projectOntologies = ImmutableSet.of(projectOntology);
        var externalOntologies = ImmutableSet.of(externalOntology);

        calculator = new ModifiedProjectOntologiesCalculator(projectOntologies, externalOntologies, diffCalculator);

        when(diffCalculator.computeDiff(projectOntology, externalOntology)).thenReturn(ontologyDiff);
    }

    @Test
    public void shouldNotPerformDiffForDifferentOntologyIRIs() {
        OWLOntologyID projectOntologyId = new OWLOntologyID(Optional.of(IRI.create("http://ontology.iri.a")), Optional.absent());
        when(projectOntology.getOntologyId()).thenReturn(projectOntologyId);
        OWLOntologyID externalOntologyId = new OWLOntologyID(Optional.of(IRI.create("http://ontology.iri.b")), Optional.absent());
        when(externalOntology.getOntologyId()).thenReturn(externalOntologyId);
        assertThat(calculator.getModifiedOntologyDiffs(), is(empty()));
    }

    @Test
    public void shouldPerformDiffForSameOntologyIRIs() {
        IRI commonIRI = IRI.create("http://ontology.iri");
        OWLOntologyID commonOntologyId = new OWLOntologyID(Optional.of(commonIRI), Optional.absent());
        when(projectOntology.getOntologyId()).thenReturn(commonOntologyId);
        when(externalOntology.getOntologyId()).thenReturn(commonOntologyId);

        assertThat(calculator.getModifiedOntologyDiffs(), contains(ontologyDiff));
    }

    @Test
    public void shouldIgnoreVersionIRI() {
        IRI commonIRI = IRI.create("http://ontology.iri");
        OWLOntologyID projectOntologyId = new OWLOntologyID(Optional.of(commonIRI), Optional.of(IRI.create("http://version.iri.a")));
        when(projectOntology.getOntologyId()).thenReturn(projectOntologyId);
        OWLOntologyID externalOntologyId = new OWLOntologyID(Optional.of(commonIRI), Optional.of(IRI.create("http://version.iri.b")));
        when(externalOntology.getOntologyId()).thenReturn(externalOntologyId);

        assertThat(calculator.getModifiedOntologyDiffs(), contains(ontologyDiff));
    }

    @Test
    public void shouldIgnoreAnonymousOntologies() {
        when(projectOntology.getOntologyId()).thenReturn(new OWLOntologyID());
        when(externalOntology.getOntologyId()).thenReturn(new OWLOntologyID());
        assertThat(calculator.getModifiedOntologyDiffs(), is(empty()));
    }
}

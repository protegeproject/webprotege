package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.project.Ontology;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyDiffCalculator_TestCase {

    private OntologyDiffCalculator calculator;

    @Mock
    private AnnotationDiffCalculator annotationDiffCalculator;

    @Mock
    private AxiomDiffCalculator axiomDiffCalculator;

    @Mock
    private Ontology fromOnt, toOnt;

    @Mock
    private Diff<OWLAnnotation> annotationDiff;

    @Mock
    private Diff<OWLAxiom> axiomDiff;

    @Mock
    private OWLOntologyID fromOntId, toOntId;

    private OntologyDiff diff;

    @Before
    public void setUp() {
        calculator = new OntologyDiffCalculator(annotationDiffCalculator,
                                                axiomDiffCalculator);

        when(fromOnt.getOntologyId())
                .thenReturn(fromOntId);

        when(toOnt.getOntologyId())
                .thenReturn(toOntId);

        when(annotationDiffCalculator.computeDiff(fromOnt, toOnt))
                .thenReturn(annotationDiff);

        when(axiomDiffCalculator.computeDiff(fromOnt, toOnt))
                .thenReturn(axiomDiff);

        diff = calculator.computeDiff(fromOnt, toOnt);
    }

    @Test
    public void shouldGetFromOntologyId() {
        assertThat(diff.getFromOntologyId(), is(fromOntId));
    }

    @Test
    public void shouldGetToOntologyId() {
        assertThat(diff.getToOntologyId(), is(toOntId));
    }

    @Test
    public void shouldGetAnnotationDiff() {
        assertThat(diff.getAnnotationDiff(), is(annotationDiff));
    }

    @Test
    public void shouldGetAxiomDiff() {
        assertThat(diff.getAxiomDiff(), is(axiomDiff));
    }
}

package edu.stanford.bmir.protege.web.shared.merge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/03/15
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyDiff_TestCase {


    private OntologyDiff ontologyDiff;

    private OntologyDiff otherOntologyDiff;

    private OWLOntologyID fromOntologyId = new OWLOntologyID();

    private OWLOntologyID toOntologyId = new OWLOntologyID();

    @Mock
    private Diff<OWLAnnotation> annotationDiff;

    @Mock
    private Diff<OWLAxiom> axiomDiff;


    @Before
    public void setUp() throws Exception {
        ontologyDiff = new OntologyDiff(fromOntologyId, toOntologyId, annotationDiff, axiomDiff);
        otherOntologyDiff = new OntologyDiff(fromOntologyId, toOntologyId, annotationDiff, axiomDiff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_FromOntologyId_IsNull() {
        new OntologyDiff(null, toOntologyId, annotationDiff, axiomDiff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ToOntologyId_IsNull() {
        new OntologyDiff(fromOntologyId, null, annotationDiff, axiomDiff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_AnnotationDiff_IsNull() {
        new OntologyDiff(fromOntologyId, toOntologyId, null, axiomDiff);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_AxiomDiff_IsNull() {
        new OntologyDiff(fromOntologyId, toOntologyId, annotationDiff, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(ontologyDiff, is(equalTo(ontologyDiff)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(ontologyDiff, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(ontologyDiff, is(equalTo(otherOntologyDiff)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(ontologyDiff.hashCode(), is(otherOntologyDiff.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(ontologyDiff.toString(), startsWith("OntologyDiff"));
    }

    @Test
    public void shouldReturnSupplied_FromOntologyId() {
        assertThat(ontologyDiff.getFromOntologyId(), is(fromOntologyId));
    }

    @Test
    public void shouldReturnSupplied_ToOntologyId() {
        assertThat(ontologyDiff.getToOntologyId(), is(toOntologyId));
    }

    @Test
    public void shouldReturnSupplied_AnnotationDiff() {
        assertThat(ontologyDiff.getAnnotationDiff(), is(annotationDiff));
    }

    @Test
    public void shouldReturnSupplied_AxiomDiff() {
        assertThat(ontologyDiff.getAxiomDiff(), is(axiomDiff));
    }
}
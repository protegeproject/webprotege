package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.diff.OntologyDiff2OntologyChanges;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyDiff2Changes_TestCase {

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OntologyDiff ontologyDiff;

    @Mock
    private Diff<OWLAxiom> axiomDiff;

    @Mock
    private OWLAxiom addedAxiom;

    @Mock
    private OWLAxiom removedAxiom;

    @Mock
    private Diff<OWLAnnotation> annotationDiff;

    @Mock
    private OWLAnnotation addedAnnotation;

    @Mock
    private OWLAnnotation removedAnnotation;


    private List<OntologyChange> ontologyChanges;

    private OntologyDiff2OntologyChanges diff2Changes;

    @Before
    public void setUp() {
        when(ontologyDiff.getAxiomDiff())
                .thenReturn(axiomDiff);

        when(ontologyDiff.getFromOntologyId())
                .thenReturn(ontologyId);



        when(axiomDiff.getAdded())
                .thenReturn(ImmutableSet.of(addedAxiom));
        when(axiomDiff.getRemoved())
                .thenReturn(ImmutableSet.of(removedAxiom));

        when(ontologyDiff.getAnnotationDiff())
                .thenReturn(annotationDiff);
        when(annotationDiff.getAdded())
                .thenReturn(ImmutableSet.of(addedAnnotation));
        when(annotationDiff.getRemoved())
                .thenReturn(ImmutableSet.of(removedAnnotation));

        diff2Changes = new OntologyDiff2OntologyChanges();
        ontologyChanges = diff2Changes.getOntologyChangesFromDiff(ontologyDiff);

    }

    @Test
    public void shouldGenerateAddAxioms() {
        assertThat(ontologyChanges, hasItem(AddAxiomChange.of(ontologyId, addedAxiom)));
    }

    @Test
    public void shouldGenerateRemoveAxioms() {
        assertThat(ontologyChanges, hasItem(RemoveAxiomChange.of(ontologyId, removedAxiom)));
    }

    @Test
    public void shouldGenerateAddAnnotations() {
        assertThat(ontologyChanges, hasItem(AddOntologyAnnotationChange.of(ontologyId, addedAnnotation)));
    }

    @Test
    public void shouldGenerateRemvoveAnnotations() {
        assertThat(ontologyChanges, hasItem(RemoveOntologyAnnotationChange.of(ontologyId, removedAnnotation)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyDiffIsNull() {
        diff2Changes.getOntologyChangesFromDiff(null);
    }
}

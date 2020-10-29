package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-18
 */
@RunWith(MockitoJUnitRunner.class)
public class RootIndexImpl_TestCase {

    private RootIndexImpl impl;

    @Mock
    private OntologyAxiomsIndex ontologyAxiomsIndex;

    @Mock
    private OntologyAnnotationsIndex ontologyAnnotationIndex;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLAnnotation annotation;

    @Before
    public void setUp() {
        when(ontologyAxiomsIndex.containsAxiom(axiom, ontologyDocumentId))
                .thenReturn(true);
        when(ontologyAnnotationIndex.containsAnnotation(annotation, ontologyDocumentId))
                .thenReturn(true);
        impl = new RootIndexImpl(ontologyAxiomsIndex,
                                 ontologyAnnotationIndex);
    }

    @Test
    public void shouldNotAddAxiomInOntology() {
        var change = AddAxiomChange.of(ontologyDocumentId, axiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }

    @Test
    public void shouldAddAxiomNotInOntology() {
        var otherAxiom = mock(OWLAxiom.class);
        var change = AddAxiomChange.of(ontologyDocumentId, otherAxiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldRemoveAxiomInOntology() {
        var change = RemoveAxiomChange.of(ontologyDocumentId, axiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldNotRemoveAxiomNotInOntology() {
        var otherAxiom = mock(OWLAxiom.class);
        var change = RemoveAxiomChange.of(ontologyDocumentId, otherAxiom);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }


    @Test
    public void shouldNotAddAnnotationInOntology() {
        var change = AddOntologyAnnotationChange.of(ontologyDocumentId, annotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }

    @Test
    public void shouldAddAnnotationNotInOntology() {
        var otherAnnotation = mock(OWLAnnotation.class);
        var change = AddOntologyAnnotationChange.of(ontologyDocumentId, otherAnnotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldRemoveAnnotationInOntology() {
        var change = RemoveOntologyAnnotationChange.of(ontologyDocumentId, annotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, contains(change));
    }

    @Test
    public void shouldNotRemoveAnnotationNotInOntology() {
        var otherAnnotation = mock(OWLAnnotation.class);
        var change = RemoveOntologyAnnotationChange.of(ontologyDocumentId, otherAnnotation);
        var effectiveChanges = getEffectiveChanges(change);
        assertThat(effectiveChanges, is(empty()));
    }

    @Test
    public void shouldFilterOutDuplicateChanges() {
        var otherAxiom = mock(OWLAxiom.class);
        var firstChange = AddAxiomChange.of(ontologyDocumentId, otherAxiom);
        var secondChange = AddAxiomChange.of(ontologyDocumentId, otherAxiom);
        var changes = ImmutableList.<OntologyChange>of(
                firstChange,
                secondChange
        );
        var effectiveChanges = impl.getEffectiveChanges(changes);
        assertThat(effectiveChanges, contains(firstChange));
        assertThat(effectiveChanges.size(), is(1));
    }

    @Test
    public void shouldFilterOutCancellingChanges() {
        var otherAxiom = mock(OWLAxiom.class);
        var firstChange = AddAxiomChange.of(ontologyDocumentId, otherAxiom);
        var secondChange = RemoveAxiomChange.of(ontologyDocumentId, otherAxiom);
        var changes = ImmutableList.<OntologyChange>of(
                firstChange,
                secondChange
        );
        var effectiveChanges = impl.getEffectiveChanges(changes);
        assertThat(effectiveChanges, is(empty()));
    }

    private List<OntologyChange> getEffectiveChanges(OntologyChange change) {
        var changes = getChangeList(change);
        return impl.getEffectiveChanges(changes);
    }

    private static ImmutableList<OntologyChange> getChangeList(OntologyChange change) {
        return ImmutableList.of(change);
    }
}

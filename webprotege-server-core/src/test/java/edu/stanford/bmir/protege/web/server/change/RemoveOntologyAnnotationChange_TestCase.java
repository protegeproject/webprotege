package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.RemoveOntologyAnnotationData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveOntologyAnnotationChange_TestCase<R> {

    private RemoveOntologyAnnotationChange change;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Mock
    private Set<OWLEntity> signature = Collections.singleton(mock(OWLEntity.class));

    @Mock
    private IriReplacer iriReplacer;

    @Mock
    private OntologyChangeVisitor visitor;

    @Mock
    private OntologyChangeVisitorEx<R> changeVisitorEx;

    @Before
    public void setUp() {
        change = RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        when(ontologyAnnotation.getSignature())
                .thenReturn(signature);
        when(iriReplacer.replaceIris(ontologyAnnotation))
                .thenReturn(ontologyAnnotation);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(change.getOntologyId(), is(ontologyId));
    }

    @Test
    public void shouldGetSuppliedAnnotation() {
        assertThat(change.getAnnotation(), is(ontologyAnnotation));
    }

    @Test
    public void shouldGetSignature() {
        assertThat(change.getSignature(), is(signature));
    }

    @Test
    public void shouldReturnIsAxiomChange() {
        assertThat(change.isAxiomChange(), is(false));
    }

    @Test
    public void shouldReturnIsAddAxiom() {
        assertThat(change.isAddAxiom(), is(false));
    }

    @Test
    public void shouldReturnIsRemoveAxiom() {
        assertThat(change.isRemoveAxiom(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetAxiomOrThrow() {
        change.getAxiomOrThrow();
    }

    @Test
    public void shouldReturnIsNotChangeForAxiomType() {
        AxiomType.AXIOM_TYPES.forEach(axiomType -> assertThat(change.isChangeFor(axiomType), is(false)));

    }

    @Test
    public void shouldReplaceIris() {
        assertThat(change.replaceIris(iriReplacer), is(change));
    }

    @Test
    public void shouldVisitChange() {
        change.accept(visitor);
        verify(visitor, times(1)).visit(change);
    }

    @Test
    public void shouldVisitChangeEx() {
        change.accept(changeVisitorEx);
        verify(changeVisitorEx, times(1)).visit(change);
    }

    @Test
    public void shouldReturnIsRemoveOntologyAnnotation() {
        assertThat(change.isRemoveOntologyAnnotation(), is(true));
    }

    @Test
    public void shouldReturnIsAddOntologyAnnotation() {
        assertThat(change.isAddOntologyAnnotation(), is(false));
    }

    @Test
    public void shouldCreateOwlOntologyChangeRecord() {
        var changeRecord = change.toOwlOntologyChangeRecord();
        assertThat(changeRecord.getOntologyID(), is(ontologyId));
        assertThat(changeRecord.getData(), is(new RemoveOntologyAnnotationData(ontologyAnnotation)));
    }

    public void shouldGetAnnotationOrThrow() {
        assertThat(change.getAnnotation(), is(ontologyAnnotation));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetImportsDeclarationOrThrow() {
        change.getImportsDeclarationOrThrow();
    }

    @Test
    public void shouldGetRevertingChange() {
        var revertingChange = change.getRevertingChange();
        assertThat(revertingChange, is(instanceOf(AddOntologyAnnotationChange.class)));
        assertThat(revertingChange.getOntologyId(), is(ontologyId));
        assertThat(revertingChange.getAnnotation(), is(ontologyAnnotation));
    }
}

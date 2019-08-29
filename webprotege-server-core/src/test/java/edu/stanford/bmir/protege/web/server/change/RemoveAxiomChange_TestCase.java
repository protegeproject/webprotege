package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
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
 * 2019-08-27
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveAxiomChange_TestCase<R> {

    private RemoveAxiomChange change;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationAssertionAxiom axiom;


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
        change = RemoveAxiomChange.of(ontologyId, axiom);
        when(axiom.getSignature())
                .thenReturn(signature);
        when(axiom.getAxiomType())
                .thenAnswer(invocation -> AxiomType.ANNOTATION_ASSERTION);

        when(iriReplacer.replaceIris(axiom))
                .thenReturn(axiom);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(change.getOntologyId(), is(ontologyId));
    }

    @Test
    public void shouldGetSuppliedAxiom() {
        assertThat(change.getAxiom(), is(axiom));
    }

    @Test
    public void shouldGetSignature() {
        assertThat(change.getSignature(), is(signature));
    }

    @Test
    public void shouldReturnIsAxiomChange() {
        assertThat(change.isAxiomChange(), is(true));
    }

    @Test
    public void shouldReturnIsRemoveAxiom() {
        assertThat(change.isRemoveAxiom(), is(true));
    }

    @Test
    public void shouldReturnIsAddAxiom() {
        assertThat(change.isAddAxiom(), is(false));
    }

    @Test
    public void shouldGetAxiomOrThrow() {
        assertThat(change.getAxiomOrThrow(), is(axiom));
    }

    @Test
    public void shouldReturnIsChangeForAxiomType() {
        assertThat(change.isChangeFor(AxiomType.ANNOTATION_ASSERTION), is(true));
    }

    @Test
    public void shouldReturnIsNotChangeForAxiomType() {
        assertThat(change.isChangeFor(AxiomType.CLASS_ASSERTION), is(false));
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
        assertThat(change.isRemoveOntologyAnnotation(), is(false));
    }

    @Test
    public void shouldReturnIsAddOntologyAnnotation() {
        assertThat(change.isAddOntologyAnnotation(), is(false));
    }

    @Test
    public void shouldCreateOwlOntologyChangeRecord() {
        var changeRecord = change.toOwlOntologyChangeRecord();
        assertThat(changeRecord.getOntologyID(), is(ontologyId));
        assertThat(changeRecord.getData(), is(new RemoveAxiomData(axiom)));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetAnnotationOrThrow() {
        change.getAnnotationOrThrow();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetImportsDeclarationOrThrow() {
        change.getImportsDeclarationOrThrow();
    }

    @Test
    public void shouldGetRevertingChange() {
        var revertingChange = change.getRevertingChange();
        assertThat(revertingChange, is(instanceOf(AddAxiomChange.class)));
        assertThat(revertingChange.getOntologyId(), is(ontologyId));
        assertThat(revertingChange.getAxiom(), is(axiom));
    }
}

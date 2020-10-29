package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
@RunWith(MockitoJUnitRunner.class)
public class AddAxiomChange_TestCase<R> {


    private AddAxiomChange change;

    @Mock
    private OntologyDocumentId ontologyId;

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
        change = AddAxiomChange.of(ontologyId, axiom);
        Mockito.when(axiom.getSignature())
               .thenReturn(signature);
        Mockito.when(axiom.getAxiomType())
               .thenAnswer(invocation -> AxiomType.ANNOTATION_ASSERTION);

        Mockito.when(iriReplacer.replaceIris(axiom))
               .thenReturn(axiom);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(change.getOntologyDocumentId(), is(ontologyId));
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
    public void shouldReturnIsAddAxiom() {
        assertThat(change.isAddAxiom(), is(true));
    }

    @Test
    public void shouldReturnIsRemoveAxiom() {
        assertThat(change.isRemoveAxiom(), is(false));
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
        Mockito.verify(visitor, Mockito.times(1)).visit(change);
    }

    @Test
    public void shouldVisitChangeEx() {
        change.accept(changeVisitorEx);
        Mockito.verify(changeVisitorEx, Mockito.times(1)).visit(change);
    }

    @Test
    public void shouldReturnIsRemoveOntologyAnnotation() {
        assertThat(change.isRemoveOntologyAnnotation(), is(false));
    }

    @Test
    public void shouldReturnIsAddOntologyAnnotation() {
        assertThat(change.isAddOntologyAnnotation(), is(false));
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
        var revertingChange = change.getInverseChange();
        assertThat(revertingChange, is(Matchers.instanceOf(RemoveAxiomChange.class)));
        assertThat(revertingChange.getOntologyDocumentId(), is(ontologyId));
        assertThat(revertingChange.getAxiom(), is(axiom));
    }

    @Test
    public void shouldReplaceOntologyId() {
        var otherOntologyId = mock(OntologyDocumentId.class);
        var replaced = change.replaceOntologyId(otherOntologyId);
        assertThat(replaced.getOntologyDocumentId(), is(otherOntologyId));
    }

    @Test
    public void shouldReturnSameForSameOntologyId() {
        var replaced = change.replaceOntologyId(ontologyId);
        assertThat(replaced, is(Matchers.sameInstance(change)));
    }
}

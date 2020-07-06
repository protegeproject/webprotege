package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.RemoveImportData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveImportChange_TestCase<R> {

    private RemoveImportChange change;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLImportsDeclaration importsDeclaration;

    @Mock
    private IriReplacer iriReplacer;

    @Mock
    private OntologyChangeVisitor visitor;

    @Mock
    private OntologyChangeVisitorEx<R> changeVisitorEx;

    @Before
    public void setUp() {
        change = RemoveImportChange.of(ontologyId, importsDeclaration);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(change.getOntologyId(), is(ontologyId));
    }

    @Test
    public void shouldGetSuppliedDeclaration() {
        assertThat(change.getImportsDeclaration(), is(importsDeclaration));
    }

    @Test
    public void shouldGetSignature() {
        assertThat(change.getSignature(), is(Matchers.empty()));
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
        assertThat(changeRecord.getData(), is(new RemoveImportData(importsDeclaration)));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetAnnotationOrThrow() {
        change.getAnnotationOrThrow();
    }

    public void shouldGetImportsDeclarationOrThrow() {
        assertThat(change.getImportsDeclarationOrThrow(), is(importsDeclaration));
    }

    @Test
    public void shouldGetRevertingChange() {
        var revertingChange = change.getInverseChange();
        assertThat(revertingChange, is(Matchers.instanceOf(AddImportChange.class)));
        assertThat(revertingChange.getOntologyId(), is(ontologyId));
        assertThat(revertingChange.getImportsDeclaration(), is(importsDeclaration));
    }

    @Test
    public void shouldReplaceOntologyId() {
        var otherOntologyId = Mockito.mock(OWLOntologyID.class);
        var replaced = change.replaceOntologyId(otherOntologyId);
        assertThat(replaced.getOntologyId(), is(otherOntologyId));
    }

    @Test
    public void shouldReturnSameForSameOntologyId() {
        var replaced = change.replaceOntologyId(ontologyId);
        assertThat(replaced, is(Matchers.sameInstance(change)));
    }
}

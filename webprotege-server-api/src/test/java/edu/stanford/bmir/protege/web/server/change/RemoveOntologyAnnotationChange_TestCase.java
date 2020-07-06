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
import org.semanticweb.owlapi.change.RemoveOntologyAnnotationData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;

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
    private Set<OWLEntity> signature = Collections.singleton(Mockito.mock(OWLEntity.class));

    @Mock
    private IriReplacer iriReplacer;

    @Mock
    private OntologyChangeVisitor visitor;

    @Mock
    private OntologyChangeVisitorEx<R> changeVisitorEx;

    @Before
    public void setUp() {
        change = RemoveOntologyAnnotationChange.of(ontologyId, ontologyAnnotation);
        Mockito.when(ontologyAnnotation.getSignature())
               .thenReturn(signature);
        Mockito.when(iriReplacer.replaceIris(ontologyAnnotation))
               .thenReturn(ontologyAnnotation);
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        MatcherAssert.assertThat(change.getOntologyId(), Matchers.is(ontologyId));
    }

    @Test
    public void shouldGetSuppliedAnnotation() {
        MatcherAssert.assertThat(change.getAnnotation(), Matchers.is(ontologyAnnotation));
    }

    @Test
    public void shouldGetSignature() {
        MatcherAssert.assertThat(change.getSignature(), Matchers.is(signature));
    }

    @Test
    public void shouldReturnIsAxiomChange() {
        MatcherAssert.assertThat(change.isAxiomChange(), Matchers.is(false));
    }

    @Test
    public void shouldReturnIsAddAxiom() {
        MatcherAssert.assertThat(change.isAddAxiom(), Matchers.is(false));
    }

    @Test
    public void shouldReturnIsRemoveAxiom() {
        MatcherAssert.assertThat(change.isRemoveAxiom(), Matchers.is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetAxiomOrThrow() {
        change.getAxiomOrThrow();
    }

    @Test
    public void shouldReturnIsNotChangeForAxiomType() {
        AxiomType.AXIOM_TYPES.forEach(axiomType -> MatcherAssert.assertThat(change.isChangeFor(axiomType), Matchers.is(false)));

    }

    @Test
    public void shouldReplaceIris() {
        MatcherAssert.assertThat(change.replaceIris(iriReplacer), Matchers.is(change));
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
        MatcherAssert.assertThat(change.isRemoveOntologyAnnotation(), Matchers.is(true));
    }

    @Test
    public void shouldReturnIsAddOntologyAnnotation() {
        MatcherAssert.assertThat(change.isAddOntologyAnnotation(), Matchers.is(false));
    }

    @Test
    public void shouldCreateOwlOntologyChangeRecord() {
        var changeRecord = change.toOwlOntologyChangeRecord();
        MatcherAssert.assertThat(changeRecord.getOntologyID(), Matchers.is(ontologyId));
        MatcherAssert.assertThat(changeRecord.getData(), Matchers.is(new RemoveOntologyAnnotationData(ontologyAnnotation)));
    }

    public void shouldGetAnnotationOrThrow() {
        MatcherAssert.assertThat(change.getAnnotation(), Matchers.is(ontologyAnnotation));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetImportsDeclarationOrThrow() {
        change.getImportsDeclarationOrThrow();
    }

    @Test
    public void shouldGetRevertingChange() {
        var revertingChange = change.getInverseChange();
        MatcherAssert.assertThat(revertingChange, Matchers.is(Matchers.instanceOf(AddOntologyAnnotationChange.class)));
        MatcherAssert.assertThat(revertingChange.getOntologyId(), Matchers.is(ontologyId));
        MatcherAssert.assertThat(revertingChange.getAnnotation(), Matchers.is(ontologyAnnotation));
    }

    @Test
    public void shouldReplaceOntologyId() {
        var otherOntologyId = Mockito.mock(OWLOntologyID.class);
        var replaced = change.replaceOntologyId(otherOntologyId);
        MatcherAssert.assertThat(replaced.getOntologyId(), Matchers.is(otherOntologyId));
    }

    @Test
    public void shouldReturnSameForSameOntologyId() {
        var replaced = change.replaceOntologyId(ontologyId);
        MatcherAssert.assertThat(replaced, Matchers.is(Matchers.sameInstance(change)));
    }
}

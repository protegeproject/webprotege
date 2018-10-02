package edu.stanford.bmir.protege.web.server.change;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jul 16
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangeListMinimiser_TestCase {

    private ChangeListMinimiser minimizer;

    private List<OWLOntologyChange> changes;

    @Mock
    private OWLAxiom axiomA;

    @Mock
    private OWLOntology ontology, otherOntology;

    private AddAxiom addAxiomA;

    private RemoveAxiom removeAxiomA;

    @Before
    public void setUp() throws Exception {
        minimizer = new ChangeListMinimiser();
        changes = new ArrayList<>();
        addAxiomA = new AddAxiom(ontology, axiomA);
        removeAxiomA = new RemoveAxiom(ontology, axiomA);
    }

    @Test
    public void shouldPreserveSingleAddAxiom() {
        changes.add(addAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(addAxiomA));
    }

    @Test
    public void shouldPreserveSingleRemoveAxiom() {
        changes.add(removeAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(removeAxiomA));
    }

    @Test
    public void shouldPreserveNonAxiomChanges() {
        AddOntologyAnnotation change = mock(AddOntologyAnnotation.class);
        changes.add(change);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(change));
    }

    @Test
    public void shouldCancelAdditionFollowedByRemoval() {
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, is(empty()));
    }

    @Test
    public void shouldCancelFirstAdditionFollowedByRemoval() {
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        changes.add(addAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(addAxiomA));
    }

    @Test
    public void shouldCancelFirstAndSecondAdditionFollowedByRemoval() {
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, is(empty()));
    }

    @Test
    public void shouldCollapseMultipleAdds() {
        changes.add(addAxiomA);
        changes.add(addAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(addAxiomA));
    }

    @Test
    public void shouldCancelRemovalFollowedByAddition() {
        changes.add(removeAxiomA);
        changes.add(addAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, is(empty()));
    }

    @Test
    public void shouldCollapseMultipleRemoves() {
        changes.add(removeAxiomA);
        changes.add(removeAxiomA);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(removeAxiomA));
    }

    @Test
    public void shouldBeSensitiveToOntologyForRemoveThenAdd() {
        RemoveAxiom rem = new RemoveAxiom(ontology, axiomA);
        changes.add(rem);
        AddAxiom add = new AddAxiom(otherOntology, axiomA);
        changes.add(add);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(rem, add));
    }

    @Test
    public void shouldBeSensitiveToOntologyForAddThenRemove() {
        RemoveAxiom rem = new RemoveAxiom(ontology, axiomA);
        AddAxiom add = new AddAxiom(otherOntology, axiomA);
        changes.add(add);
        changes.add(rem);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(add, rem));
    }

    @Test
    public void shouldBeSensitiveToOntologyForAddThenAdd() {
        AddAxiom add = new AddAxiom(ontology, axiomA);
        AddAxiom otherAdd = new AddAxiom(otherOntology, axiomA);
        changes.add(add);
        changes.add(otherAdd);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(add, otherAdd));
    }

    @Test
    public void shouldBeSensitiveToOntologyForRemoveThenRemove() {
        RemoveAxiom rem = new RemoveAxiom(ontology, axiomA);
        RemoveAxiom otherRem = new RemoveAxiom(otherOntology, axiomA);
        changes.add(rem);
        changes.add(otherRem);
        List<OWLOntologyChange> minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(rem, otherRem));
    }

}

package edu.stanford.bmir.protege.web.server.change;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jul 16
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangeListMinimiser_TestCase {

    private ChangeListMinimiser minimizer;

    private List<OntologyChange> changes;

    @Mock
    private OWLAxiom axiomA;

    @Mock
    private OWLOntologyID ontologyId, otherOntologyId;

    private AddAxiomChange addAxiomA;

    private RemoveAxiomChange removeAxiomA;

    @Before
    public void setUp() throws Exception {
        minimizer = new ChangeListMinimiser();
        changes = new ArrayList<>();
        addAxiomA = AddAxiomChange.of(ontologyId, axiomA);
        removeAxiomA = RemoveAxiomChange.of(ontologyId, axiomA);
    }

    @Test
    public void shouldPreserveSingleAddAxiom() {
        changes.add(addAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(addAxiomA));
    }

    @Test
    public void shouldPreserveSingleRemoveAxiom() {
        changes.add(removeAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(removeAxiomA));
    }

    @Test
    public void shouldPreserveNonAxiomChanges() {
        AddOntologyAnnotationChange change = mock(AddOntologyAnnotationChange.class);
        changes.add(change);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(change));
    }

    @Test
    public void shouldCancelAdditionFollowedByRemoval() {
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, is(empty()));
    }

    @Test
    public void shouldCancelFirstAdditionFollowedByRemoval() {
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        changes.add(addAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(addAxiomA));
    }

    @Test
    public void shouldCancelFirstAndSecondAdditionFollowedByRemoval() {
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        changes.add(addAxiomA);
        changes.add(removeAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, is(empty()));
    }

    @Test
    public void shouldCollapseMultipleAdds() {
        changes.add(addAxiomA);
        changes.add(addAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(addAxiomA));
    }

    @Test
    public void shouldCancelRemovalFollowedByAddition() {
        changes.add(removeAxiomA);
        changes.add(addAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, is(empty()));
    }

    @Test
    public void shouldCollapseMultipleRemoves() {
        changes.add(removeAxiomA);
        changes.add(removeAxiomA);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(removeAxiomA));
    }

    @Test
    public void shouldBeSensitiveToOntologyForRemoveThenAdd() {
        RemoveAxiomChange rem = RemoveAxiomChange.of(ontologyId, axiomA);
        changes.add(rem);
        AddAxiomChange add = AddAxiomChange.of(otherOntologyId, axiomA);
        changes.add(add);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(rem, add));
    }

    @Test
    public void shouldBeSensitiveToOntologyForAddThenRemove() {
        var rem = RemoveAxiomChange.of(ontologyId, axiomA);
        var add = AddAxiomChange.of(otherOntologyId, axiomA);
        changes.add(add);
        changes.add(rem);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(add, rem));
    }

    @Test
    public void shouldBeSensitiveToOntologyForAddThenAdd() {
        var add = AddAxiomChange.of(ontologyId, axiomA);
        var otherAdd = AddAxiomChange.of(otherOntologyId, axiomA);
        changes.add(add);
        changes.add(otherAdd);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(add, otherAdd));
    }

    @Test
    public void shouldBeSensitiveToOntologyForRemoveThenRemove() {
        var rem = RemoveAxiomChange.of(ontologyId, axiomA);
        var otherRem = RemoveAxiomChange.of(otherOntologyId, axiomA);
        changes.add(rem);
        changes.add(otherRem);
        var minimizedChanges = minimizer.getMinimisedChanges(changes);
        assertThat(minimizedChanges, contains(rem, otherRem));
    }

}

package edu.stanford.bmir.protege.web.server.change;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyChangeList_TestCase<R> {


    private OntologyChangeList.Builder<R> changeListBuilder;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private R subject;

    @Mock
    private OntologyChange ontologyChange;

    @Before
    public void setUp() {
        changeListBuilder = OntologyChangeList.builder();
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfResultIsNull() {
        changeListBuilder.build(null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAddOntologyIdIsNull() {
        changeListBuilder.addAxiom(null, axiom);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfAddAxiomIdIsNull() {
        changeListBuilder.addAxiom(ontologyId, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfRemoveOntologyIdIsNull() {
        changeListBuilder.removeAxiom(null, axiom);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfRemoveAxiomIdIsNull() {
        changeListBuilder.removeAxiom(ontologyId, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfChangeIsNull() {
        changeListBuilder.add(null);
    }

    @Test
    public void shouldBuildEmptyChangeList() {
        var changeList = changeListBuilder.build(subject);
        assertThat(changeList.getChanges(), is(empty()));
    }

    @Test
    public void shouldBuildChangeListWithSuppliedResult() {
        var changeList = changeListBuilder.build(subject);
        assertThat(changeList.getResult(), is(subject));
    }

    @Test
    public void shouldBuildAddAxiom() {
        changeListBuilder.addAxiom(ontologyId, axiom);
        assertChangeListContains(AddAxiomChange.of(ontologyId, axiom));
    }
    
    @Test
    public void shouldBuildRemoveAxiom() {
        changeListBuilder.removeAxiom(ontologyId, axiom);
        assertChangeListContains(RemoveAxiomChange.of(ontologyId, axiom));
    }

    @Test
    public void shouldBuildWithChange() {
        changeListBuilder.add(ontologyChange);
        assertChangeListContains(ontologyChange);
    }

    private void assertChangeListContains(OntologyChange change) {
        var changeList = changeListBuilder.build(subject);
        assertThat(changeList.getChanges(), hasItem(change));
    }

    @Test
    public void shouldBeNonEmpty() {
        changeListBuilder.add(ontologyChange);
        assertThat(changeListBuilder.isEmpty(), is(false));
    }
    @Test
    public void shouldBeEmpty() {
        assertThat(changeListBuilder.isEmpty(), is(true));
    }
}

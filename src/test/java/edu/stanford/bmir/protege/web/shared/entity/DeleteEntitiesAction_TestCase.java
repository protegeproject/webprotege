
package edu.stanford.bmir.protege.web.shared.entity;

import java.util.HashSet;
import java.util.Set;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DeleteEntitiesAction_TestCase {

    private DeleteEntitiesAction deleteEntitiesAction;

    @Mock
    private ProjectId projectId;

    private Set<OWLEntity> entities = new HashSet<>();

    @Before
    public void setUp() {
        entities.add(mock(OWLEntity.class));
        deleteEntitiesAction = new DeleteEntitiesAction(projectId, entities);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new DeleteEntitiesAction(null, entities);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(deleteEntitiesAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entities_IsNull() {
        new DeleteEntitiesAction(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entities() {
        assertThat(deleteEntitiesAction.getEntities(), is(this.entities));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(deleteEntitiesAction, is(deleteEntitiesAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(deleteEntitiesAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(deleteEntitiesAction, is(new DeleteEntitiesAction(projectId, entities)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(deleteEntitiesAction, is(not(new DeleteEntitiesAction(mock(ProjectId.class), entities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entities() {
        assertThat(deleteEntitiesAction, is(not(new DeleteEntitiesAction(projectId, new HashSet<>()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(deleteEntitiesAction.hashCode(), is(new DeleteEntitiesAction(projectId, entities).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(deleteEntitiesAction.toString(), startsWith("DeleteEntitiesAction"));
    }

}


package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetHierarchyPathsToRootAction_TestCase {

    private GetHierarchyPathsToRootAction action;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    @Mock
    private HierarchyId hierarchyId;

    @Before
    public void setUp() {
        action = new GetHierarchyPathsToRootAction(projectId, entity, hierarchyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new GetHierarchyPathsToRootAction(null, entity, hierarchyId);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new GetHierarchyPathsToRootAction(projectId, null, hierarchyId);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(action.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_hierarchyId_IsNull() {
        new GetHierarchyPathsToRootAction(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_hierarchyId() {
        assertThat(action.getHierarchyId(), is(this.hierarchyId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(new GetHierarchyPathsToRootAction(projectId, entity, hierarchyId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(new GetHierarchyPathsToRootAction(mock(ProjectId.class), entity, hierarchyId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(action, is(not(new GetHierarchyPathsToRootAction(projectId, mock(OWLEntity.class), hierarchyId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_hierarchyId() {
        assertThat(action, is(not(new GetHierarchyPathsToRootAction(projectId, entity, mock(HierarchyId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(new GetHierarchyPathsToRootAction(projectId, entity, hierarchyId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("GetHierarchyPathsToRootAction"));
    }

}

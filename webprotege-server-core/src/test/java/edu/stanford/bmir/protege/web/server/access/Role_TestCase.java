
package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Role_TestCase {

    private Role role;

    @Mock
    private RoleId roleId, parentRoleId;

    @Mock
    private ActionId actionId;

    private List<RoleId> parents;

    private List<ActionId> actions;

    @Before
    public void setUp() {
        parents = singletonList(parentRoleId);
        actions = singletonList(actionId);
        role = new Role(roleId, parents, actions);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_roleId_IsNull() {
        new Role(null, parents, actions);
    }

    @Test
    public void shouldReturnSupplied_roleId() {
        assertThat(role.getRoleId(), is(this.roleId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_parents_IsNull() {
        new Role(roleId, null, actions);
    }

    @Test
    public void shouldReturnSupplied_parents() {
        assertThat(role.getParents(), is(this.parents));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_actions_IsNull() {
        new Role(roleId, parents, null);
    }

    @Test
    public void shouldReturnSupplied_actions() {
        assertThat(role.getActions(), is(this.actions));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(role, is(role));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(role.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(role, is(new Role(roleId, parents, actions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_roleId() {
        assertThat(role, is(not(new Role(mock(RoleId.class), parents, actions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_parents() {
        assertThat(role, is(not(new Role(roleId, singletonList(mock(RoleId.class)), actions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_actions() {
        assertThat(role, is(not(new Role(roleId, parents, singletonList(mock(ActionId.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(role.hashCode(), is(new Role(roleId, parents, actions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(role.toString(), Matchers.startsWith("Role"));
    }

}

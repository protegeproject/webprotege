package edu.stanford.bmir.protege.web.shared.access;


import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RoleId_TestCase {

    private RoleId roleId;

    private String id = "The id";

    @Before
    public void setUp() {
        roleId = new RoleId(id);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new RoleId(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(roleId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(roleId, is(roleId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(roleId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(roleId, is(new RoleId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(roleId, is(not(new RoleId("String-fefb9f3e-0859-40e3-89d5-06ac997b1794"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(roleId.hashCode(), is(new RoleId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(roleId.toString(), startsWith("RoleId"));
    }

}

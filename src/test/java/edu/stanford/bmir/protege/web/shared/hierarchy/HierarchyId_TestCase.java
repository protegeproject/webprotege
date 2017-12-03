
package edu.stanford.bmir.protege.web.shared.hierarchy;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class HierarchyId_TestCase {

    private static final String ID = "TheHierarchy";
    private HierarchyId hierarchyId;

    @Before
    public void setUp() {
        hierarchyId = HierarchyId.get(ID);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        HierarchyId.get(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(hierarchyId, is(hierarchyId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(hierarchyId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(hierarchyId, is(HierarchyId.get(ID)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(hierarchyId.hashCode(), is(HierarchyId.get(ID).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(hierarchyId.toString(), Matchers.startsWith("HierarchyId"));
    }
}

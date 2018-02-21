
package edu.stanford.bmir.protege.web.shared.perspective;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PerspectiveId_TestCase {

    private PerspectiveId perspectiveId;

    private String id = "The id";

    @Before
    public void setUp() throws Exception {
        perspectiveId = new PerspectiveId(id);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new PerspectiveId(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(perspectiveId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(perspectiveId, is(perspectiveId));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(perspectiveId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(perspectiveId, is(new PerspectiveId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(perspectiveId, is(not(new PerspectiveId("String-4c96af5c-31d0-4b5a-9d57-ed48e6668da4"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(perspectiveId.hashCode(), is(new PerspectiveId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(perspectiveId.toString(), startsWith("PerspectiveId"));
    }

}

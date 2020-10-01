
package edu.stanford.bmir.protege.web.shared.perspective;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ResetPerspectiveLayoutResult_TestCase {

    private ResetPerspectiveLayoutResult resetPerspectiveLayoutResult;

    @Mock
    private PerspectiveLayout resetLayout;

    @Before
    public void setUp()
        throws Exception
    {
        resetPerspectiveLayoutResult = new ResetPerspectiveLayoutResult(resetLayout);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_resetLayout_IsNull() {
        new ResetPerspectiveLayoutResult(null);
    }

    @Test
    public void shouldReturnSupplied_resetLayout() {
        assertThat(resetPerspectiveLayoutResult.getResetLayout(), is(this.resetLayout));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(resetPerspectiveLayoutResult, is(resetPerspectiveLayoutResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(resetPerspectiveLayoutResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(resetPerspectiveLayoutResult, is(new ResetPerspectiveLayoutResult(resetLayout)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_resetLayout() {
        assertThat(resetPerspectiveLayoutResult, is(not(new ResetPerspectiveLayoutResult(mock(PerspectiveLayout.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(resetPerspectiveLayoutResult.hashCode(), is(new ResetPerspectiveLayoutResult(resetLayout).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(resetPerspectiveLayoutResult.toString(), Matchers.startsWith("ResetPerspectiveLayoutResult"));
    }

}

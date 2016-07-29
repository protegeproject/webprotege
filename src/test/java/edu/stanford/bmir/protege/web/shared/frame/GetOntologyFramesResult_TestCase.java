
package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetOntologyFramesResult_TestCase {

    private GetOntologyFramesResult result;

    @Mock
    private ImmutableList ontologyFrames;

    @Before
    public void setUp()
        throws Exception
    {
        result = new GetOntologyFramesResult(ontologyFrames);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ontologyFrames_IsNull() {
        new GetOntologyFramesResult(null);
    }

    @Test
    public void shouldReturnSupplied_ontologyFrames() {
        MatcherAssert.assertThat(result.getOntologyFrames(), Matchers.is(this.ontologyFrames));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(result, Matchers.is(result));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(result.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(result, Matchers.is(new GetOntologyFramesResult(ontologyFrames)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_ontologyFrames() {
        MatcherAssert.assertThat(result, Matchers.is(Matchers.not(new GetOntologyFramesResult(Mockito.mock(ImmutableList.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(result.hashCode(), Matchers.is(new GetOntologyFramesResult(ontologyFrames).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(result.toString(), Matchers.startsWith("GetOntologyFramesResult"));
    }

}

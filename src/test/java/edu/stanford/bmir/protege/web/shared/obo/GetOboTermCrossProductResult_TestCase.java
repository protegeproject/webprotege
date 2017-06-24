
package edu.stanford.bmir.protege.web.shared.obo;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermCrossProductResult_TestCase {

    private GetOboTermCrossProductResult getOboTermCrossProductResult;
    @Mock
    private OBOTermCrossProduct crossProduct;

    @Before
    public void setUp()
        throws Exception
    {
        getOboTermCrossProductResult = new GetOboTermCrossProductResult(crossProduct);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_crossProduct_IsNull() {
        new GetOboTermCrossProductResult(null);
    }

    @Test
    public void shouldReturnSupplied_crossProduct() {
        assertThat(getOboTermCrossProductResult.getCrossProduct(), is(this.crossProduct));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermCrossProductResult, is(getOboTermCrossProductResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermCrossProductResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermCrossProductResult, is(new GetOboTermCrossProductResult(crossProduct)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_crossProduct() {
        assertThat(getOboTermCrossProductResult, is(not(new GetOboTermCrossProductResult(Mockito.mock(OBOTermCrossProduct.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermCrossProductResult.hashCode(), is(new GetOboTermCrossProductResult(crossProduct).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermCrossProductResult.toString(), startsWith("GetOboTermCrossProductResult"));
    }

}

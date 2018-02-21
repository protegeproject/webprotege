
package edu.stanford.bmir.protege.web.shared.obo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermRelationshipsResult_TestCase {

    private GetOboTermRelationshipsResult getOboTermRelationshipsResult;
    @Mock
    private OBOTermRelationships relationships;

    @Before
    public void setUp() {
        getOboTermRelationshipsResult = new GetOboTermRelationshipsResult(relationships);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_relationships_IsNull() {
        new GetOboTermRelationshipsResult(null);
    }

    @Test
    public void shouldReturnSupplied_relationships() {
        assertThat(getOboTermRelationshipsResult.getRelationships(), is(this.relationships));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermRelationshipsResult, is(getOboTermRelationshipsResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermRelationshipsResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermRelationshipsResult, is(new GetOboTermRelationshipsResult(relationships)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_relationships() {
        assertThat(getOboTermRelationshipsResult, is(not(new GetOboTermRelationshipsResult(Mockito.mock(OBOTermRelationships.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermRelationshipsResult.hashCode(), is(new GetOboTermRelationshipsResult(relationships).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermRelationshipsResult.toString(), startsWith("GetOboTermRelationshipsResult"));
    }

}

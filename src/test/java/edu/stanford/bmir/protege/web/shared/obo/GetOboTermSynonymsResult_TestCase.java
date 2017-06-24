
package edu.stanford.bmir.protege.web.shared.obo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermSynonymsResult_TestCase {

    private GetOboTermSynonymsResult getOboTermSynonymsResult;

    private List<OBOTermSynonym> synonyms = singletonList(mock(OBOTermSynonym.class));

    @Before
    public void setUp() {
        getOboTermSynonymsResult = new GetOboTermSynonymsResult(synonyms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_synonyms_IsNull() {
        new GetOboTermSynonymsResult(null);
    }

    @Test
    public void shouldReturnSupplied_synonyms() {
        assertThat(getOboTermSynonymsResult.getSynonyms(), is(this.synonyms));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermSynonymsResult, is(getOboTermSynonymsResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermSynonymsResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermSynonymsResult, is(new GetOboTermSynonymsResult(synonyms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_synonyms() {
        assertThat(getOboTermSynonymsResult, is(not(new GetOboTermSynonymsResult(singletonList(mock(OBOTermSynonym.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermSynonymsResult.hashCode(), is(new GetOboTermSynonymsResult(synonyms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermSynonymsResult.toString(), startsWith("GetOboTermSynonymsResult"));
    }

}

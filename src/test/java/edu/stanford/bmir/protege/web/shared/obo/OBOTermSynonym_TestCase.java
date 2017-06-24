
package edu.stanford.bmir.protege.web.shared.obo;

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

import static edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonymScope.BROADER;
import static edu.stanford.bmir.protege.web.shared.obo.OBOTermSynonymScope.EXACT;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OBOTermSynonym_TestCase {

    private OBOTermSynonym oBOTermSynonym;

    private List<OBOXRef> xrefs = Collections.singletonList(mock(OBOXRef.class));

    private String name = "The name";

    private OBOTermSynonymScope scope = EXACT;

    @Before
    public void setUp() {
        oBOTermSynonym = new OBOTermSynonym(xrefs, name, scope);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_xrefs_IsNull() {
        new OBOTermSynonym(null, name, scope);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_name_IsNull() {
        new OBOTermSynonym(xrefs, null, scope);
    }

    @Test
    public void shouldReturnSupplied_name() {
        assertThat(oBOTermSynonym.getName(), is(this.name));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_scope_IsNull() {
        new OBOTermSynonym(xrefs, name, null);
    }

    @Test
    public void shouldReturnSupplied_scope() {
        assertThat(oBOTermSynonym.getScope(), is(this.scope));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oBOTermSynonym, is(oBOTermSynonym));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oBOTermSynonym.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oBOTermSynonym, is(new OBOTermSynonym(xrefs, name, scope)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_xrefs() {
        assertThat(oBOTermSynonym, is(not(new OBOTermSynonym(emptyList(), name, scope))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_name() {
        assertThat(oBOTermSynonym, is(not(new OBOTermSynonym(xrefs, "String-f2dcfba9-2de4-40d9-b189-cd8ed73d3769", scope))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_scope() {
        assertThat(oBOTermSynonym, is(not(new OBOTermSynonym(xrefs, name, BROADER))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oBOTermSynonym.hashCode(), is(new OBOTermSynonym(xrefs, name, scope).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oBOTermSynonym.toString(), startsWith("OBOTermSynonym"));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        assertThat(new OBOTermSynonym(emptyList(), "", EXACT).isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        assertThat(oBOTermSynonym.isEmpty(), is(false));
    }

}

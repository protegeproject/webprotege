
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

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(value = org.mockito.runners.MockitoJUnitRunner.class)
public class OBOTermDefinition_TestCase {

    private OBOTermDefinition oBOTermDefinition;

    private List<OBOXRef> xrefs = Arrays.asList(mock(OBOXRef.class), mock(OBOXRef.class));

    private String definition = "The definition";

    @Before
    public void setUp() {
        oBOTermDefinition = new OBOTermDefinition(xrefs, definition);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_xrefs_IsNull() {
        new OBOTermDefinition(null, definition);
    }

    @Test
    public void shouldReturnSupplied_xrefs() {
        assertThat(oBOTermDefinition.getXRefs(), is(this.xrefs));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_definition_IsNull() {
        new OBOTermDefinition(xrefs, null);
    }

    @Test
    public void shouldReturnSupplied_definition() {
        assertThat(oBOTermDefinition.getDefinition(), is(this.definition));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oBOTermDefinition, is(oBOTermDefinition));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oBOTermDefinition.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oBOTermDefinition, is(new OBOTermDefinition(xrefs, definition)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_xrefs() {
        assertThat(oBOTermDefinition, is(not(new OBOTermDefinition(singletonList(mock(OBOXRef.class)), definition))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_definition() {
        assertThat(oBOTermDefinition, is(not(new OBOTermDefinition(xrefs, "String-cbc45b45-a64b-436c-b5cd-534327c6f34d"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oBOTermDefinition.hashCode(), is(new OBOTermDefinition(xrefs, definition).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oBOTermDefinition.toString(), startsWith("OBOTermDefinition"));
    }
}

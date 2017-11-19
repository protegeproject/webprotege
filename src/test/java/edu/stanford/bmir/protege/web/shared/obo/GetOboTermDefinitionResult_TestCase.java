
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
public class GetOboTermDefinitionResult_TestCase {

    private GetOboTermDefinitionResult getOboTermDefinitionResult;
    @Mock
    private OBOTermDefinition definition;

    @Before
    public void setUp() {
        getOboTermDefinitionResult = new GetOboTermDefinitionResult(definition);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_definition_IsNull() {
        new GetOboTermDefinitionResult(null);
    }

    @Test
    public void shouldReturnSupplied_definition() {
        assertThat(getOboTermDefinitionResult.getDefinition(), is(this.definition));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermDefinitionResult, is(getOboTermDefinitionResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermDefinitionResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermDefinitionResult, is(new GetOboTermDefinitionResult(definition)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_definition() {
        assertThat(getOboTermDefinitionResult, is(not(new GetOboTermDefinitionResult(Mockito.mock(OBOTermDefinition.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermDefinitionResult.hashCode(), is(new GetOboTermDefinitionResult(definition).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermDefinitionResult.toString(), startsWith("GetOboTermDefinitionResult"));
    }

}

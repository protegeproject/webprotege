
package edu.stanford.bmir.protege.web.shared.obo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class OBOTermId_TestCase {

    private OBOTermId oBOTermId;

    private String id = "The id";

    private String name = "The name";

    private String namespace = "The namespace";

    @Before
    public void setUp() {
        oBOTermId = new OBOTermId(id, name, namespace);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new OBOTermId(null, name, namespace);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(oBOTermId.getId(), is(this.id));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_name_IsNull() {
        new OBOTermId(id, null, namespace);
    }

    @Test
    public void shouldReturnSupplied_name() {
        assertThat(oBOTermId.getName(), is(this.name));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_namespace_IsNull() {
        new OBOTermId(id, name, null);
    }

    @Test
    public void shouldReturnSupplied_namespace() {
        assertThat(oBOTermId.getNamespace(), is(this.namespace));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oBOTermId, is(oBOTermId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oBOTermId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oBOTermId, is(new OBOTermId(id, name, namespace)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(oBOTermId, is(not(new OBOTermId("String-058a3768-510b-40bd-ba9f-1d92a3a5be4c", name, namespace))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_name() {
        assertThat(oBOTermId, is(not(new OBOTermId(id, "String-1b3b8b94-315d-48f6-951f-92dcecef4dff", namespace))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_namespace() {
        assertThat(oBOTermId, is(not(new OBOTermId(id, name, "String-361161d9-bc09-4605-ac63-54de51596268"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oBOTermId.hashCode(), is(new OBOTermId(id, name, namespace).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oBOTermId.toString(), startsWith("OBOTermId"));
    }

    @Test
    public void shouldReturn_true_For_isEmpty() {
        assertThat(new OBOTermId("", "", "").isEmpty(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        assertThat(oBOTermId.isEmpty(), is(false));
    }

}

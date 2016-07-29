
package edu.stanford.bmir.protege.web.shared.form;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FormId_TestCase {

    private FormId formId;

    private String id = "The id";

    @Before
    public void setUp() {
        formId = new FormId(id);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new FormId(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(formId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(formId, is(formId));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(formId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(formId, is(new FormId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(formId, is(not(new FormId("String-1c11bb8a-d00d-4d02-ab5c-84498c0f513c"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(formId.hashCode(), is(new FormId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(formId.toString(), startsWith("FormId"));
    }

}

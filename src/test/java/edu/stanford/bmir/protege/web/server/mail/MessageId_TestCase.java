
package edu.stanford.bmir.protege.web.server.mail;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class MessageId_TestCase {

    private MessageId messageId;

    private String id = "The id";

    @Before
    public void setUp() {
        messageId = new MessageId(id);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new MessageId(null);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(messageId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(messageId, is(messageId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(messageId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(messageId, is(new MessageId(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(messageId, is(not(new MessageId("String-a556b7cd-f500-442b-9598-06460f3f48cb"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(messageId.hashCode(), is(new MessageId(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(messageId.toString(), Matchers.startsWith("MessageId"));
    }

}

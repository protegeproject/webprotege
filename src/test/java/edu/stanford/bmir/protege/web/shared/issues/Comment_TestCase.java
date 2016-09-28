
package edu.stanford.bmir.protege.web.shared.issues;

import java.util.Optional;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Comment_TestCase {


    @Mock
    private UserId createdBy;

    private long createdAt = 1L;

    private Optional<Long> updatedAt = Optional.of(1L);

    private String body = "The body";

    @Mock
    private ImmutableSet<Mention> mentions;


    private Comment comment;



    @Before
    public void setUp() {
        comment = new Comment(createdBy, createdAt, updatedAt, body, mentions);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_createdBy_IsNull() {
        new Comment(null, createdAt, updatedAt, body, mentions);
    }

    @Test
    public void shouldReturnSupplied_createdBy() {
        assertThat(comment.getCreatedBy(), is(this.createdBy));
    }

    @Test
    public void shouldReturnSupplied_createdAt() {
        assertThat(comment.getCreatedAt(), is(this.createdAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_updatedAt_IsNull() {
        new Comment(createdBy, createdAt, null, body, mentions);
    }

    @Test
    public void shouldReturnSupplied_updatedAt() {
        assertThat(comment.getUpdatedAt(), is(this.updatedAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_body_IsNull() {
        new Comment(createdBy, createdAt, updatedAt, null, mentions);
    }

    @Test
    public void shouldReturnSupplied_body() {
        assertThat(comment.getBody(), is(this.body));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_mentions_IsNull() {
        new Comment(createdBy, createdAt, updatedAt, body, null);
    }

    @Test
    public void shouldReturnSupplied_mentions() {
        assertThat(comment.getMentions(), is(this.mentions));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(comment, is(comment));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(comment.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(comment, is(new Comment(createdBy, createdAt, updatedAt, body, mentions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdBy() {
        assertThat(comment, is(Matchers.not(new Comment(mock(UserId.class), createdAt, updatedAt, body, mentions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(comment, is(Matchers.not(new Comment(createdBy, 2L, updatedAt, body, mentions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_updatedAt() {
        assertThat(comment, is(Matchers.not(new Comment(createdBy, createdAt, Optional.of(2L), body, mentions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_body() {
        assertThat(comment, is(Matchers.not(new Comment(createdBy, createdAt, updatedAt, "String-1ced982f-a60a-4681-a37b-03a2e7fbb426", mentions))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_mentions() {
        assertThat(comment, is(Matchers.not(new Comment(createdBy, createdAt, updatedAt, body, mock(ImmutableSet.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(comment.hashCode(), is(new Comment(createdBy, createdAt, updatedAt, body, mentions).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(comment.toString(), Matchers.startsWith("Comment"));
    }

}

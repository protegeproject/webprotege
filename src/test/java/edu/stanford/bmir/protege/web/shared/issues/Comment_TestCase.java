
package edu.stanford.bmir.protege.web.shared.issues;

import java.util.Optional;
import edu.stanford.bmir.protege.web.shared.user.UserId;
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
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Comment_TestCase {

    private Comment comment;
    @Mock
    private CommentId id;
    @Mock
    private UserId createdBy;

    private long createdAt = 1L;

    private Optional<Long> updatedAt = Optional.of(33L);

    private String body = "The body";

    @Before
    public void setUp() {
        comment = new Comment(id, createdBy, createdAt, updatedAt, body);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new Comment(null, createdBy, createdAt, updatedAt, body);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(comment.getId(), is(this.id));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_createdBy_IsNull() {
        new Comment(id, null, createdAt, updatedAt, body);
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
        new Comment(id, createdBy, createdAt, null, body);
    }

    @Test
    public void shouldReturnSupplied_updatedAt() {
        assertThat(comment.getUpdatedAt(), is(this.updatedAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_body_IsNull() {
        new Comment(id, createdBy, createdAt, updatedAt, null);
    }

    @Test
    public void shouldReturnSupplied_body() {
        assertThat(comment.getBody(), is(this.body));
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
        assertThat(comment, is(new Comment(id, createdBy, createdAt, updatedAt, body)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(comment, is(not(new Comment(mock(CommentId.class), createdBy, createdAt, updatedAt, body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdBy() {
        assertThat(comment, is(not(new Comment(id, mock(UserId.class), createdAt, updatedAt, body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(comment, is(not(new Comment(id, createdBy, 2L, updatedAt, body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_updatedAt() {
        assertThat(comment, is(not(new Comment(id, createdBy, createdAt, Optional.of(55L), body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_body() {
        assertThat(comment, is(not(new Comment(id, createdBy, createdAt, updatedAt, "String-3765ff88-b38c-4ec4-a4bd-8403225ebe25"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(comment.hashCode(), is(new Comment(id, createdBy, createdAt, updatedAt, body).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(comment.toString(), Matchers.startsWith("Comment"));
    }

}

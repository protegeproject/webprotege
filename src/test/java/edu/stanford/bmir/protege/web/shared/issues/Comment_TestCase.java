
package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Comment_TestCase {

    private Comment comment;

    @Mock
    private UserId createdBy;

    private long createdAt = 1L;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType" )
    private Optional<Long> updatedAt;

    private String body = "The body";

    @Before
    public void setUp() {
        updatedAt = Optional.of(33L);
        comment = new Comment(createdBy, createdAt, updatedAt, body);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_createdBy_IsNull() {
        new Comment(null, createdAt, updatedAt, body);
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
        new Comment(createdBy, createdAt, null, body);
    }

    @Test
    public void shouldReturnSupplied_updatedAt() {
        assertThat(comment.getUpdatedAt(), is(this.updatedAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_body_IsNull() {
        new Comment(createdBy, createdAt, updatedAt, null);
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
        assertThat(comment, is(new Comment(createdBy, createdAt, updatedAt, body)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdBy() {
        assertThat(comment, is(not(new Comment(mock(UserId.class), createdAt, updatedAt, body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        assertThat(comment, is(not(new Comment(createdBy, 2L, updatedAt, body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_updatedAt() {
        assertThat(comment, is(not(new Comment(createdBy, createdAt, mock(Optional.class), body))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_body() {
        assertThat(comment, is(not(new Comment(createdBy, createdAt, updatedAt, "String-e426d6fd-1276-4893-868c-919466862dcd"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(comment.hashCode(), is(new Comment(createdBy, createdAt, updatedAt, body).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(comment.toString(), startsWith("Comment"));
    }

}

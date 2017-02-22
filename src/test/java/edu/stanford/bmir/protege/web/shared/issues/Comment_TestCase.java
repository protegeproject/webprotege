
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

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class Comment_TestCase {

    private Comment comment;
    @Mock
    private edu.stanford.bmir.protege.web.shared.issues.CommentId id;
    @Mock
    private UserId createdBy;
    private long createdAt = 1L;
    private Optional updatedAt = Optional.of(33L);
    private String body = "The body";
    private String renderedBody = "The renderedBody";

    @Before
    public void setUp()
        throws Exception
    {
        comment = new Comment(id, createdBy, createdAt, updatedAt, body, renderedBody);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_id_IsNull() {
        new Comment(null, createdBy, createdAt, updatedAt, body, renderedBody);
    }

    @Test
    public void shouldReturnSupplied_id() {
        MatcherAssert.assertThat(comment.getId(), Matchers.is(this.id));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_createdBy_IsNull() {
        new Comment(id, null, createdAt, updatedAt, body, renderedBody);
    }

    @Test
    public void shouldReturnSupplied_createdBy() {
        MatcherAssert.assertThat(comment.getCreatedBy(), Matchers.is(this.createdBy));
    }

    @Test
    public void shouldReturnSupplied_createdAt() {
        MatcherAssert.assertThat(comment.getCreatedAt(), Matchers.is(this.createdAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_updatedAt_IsNull() {
        new Comment(id, createdBy, createdAt, null, body, renderedBody);
    }

    @Test
    public void shouldReturnSupplied_updatedAt() {
        MatcherAssert.assertThat(comment.getUpdatedAt(), Matchers.is(this.updatedAt));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_body_IsNull() {
        new Comment(id, createdBy, createdAt, updatedAt, null, renderedBody);
    }

    @Test
    public void shouldReturnSupplied_body() {
        MatcherAssert.assertThat(comment.getBody(), Matchers.is(this.body));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_renderedBody_IsNull() {
        new Comment(id, createdBy, createdAt, updatedAt, body, null);
    }

    @Test
    public void shouldReturnSupplied_renderedBody() {
        MatcherAssert.assertThat(comment.getRenderedBody(), Matchers.is(this.renderedBody));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(comment, Matchers.is(comment));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(comment.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(comment, Matchers.is(new Comment(id, createdBy, createdAt, updatedAt, body, renderedBody)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        MatcherAssert.assertThat(comment, Matchers.is(Matchers.not(new Comment(Mockito.mock(edu.stanford.bmir.protege.web.shared.issues.CommentId.class), createdBy, createdAt, updatedAt, body, renderedBody))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdBy() {
        MatcherAssert.assertThat(comment, Matchers.is(Matchers.not(new Comment(id, Mockito.mock(UserId.class), createdAt, updatedAt, body, renderedBody))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_createdAt() {
        MatcherAssert.assertThat(comment, Matchers.is(Matchers.not(new Comment(id, createdBy, 2L, updatedAt, body, renderedBody))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_updatedAt() {
        MatcherAssert.assertThat(comment, Matchers.is(Matchers.not(new Comment(id, createdBy, createdAt, Mockito.mock(Optional.class), body, renderedBody))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_body() {
        MatcherAssert.assertThat(comment, Matchers.is(Matchers.not(new Comment(id, createdBy, createdAt, updatedAt, "String-7db11dfc-07e3-4497-b089-f32f37e0daf8", renderedBody))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_renderedBody() {
        MatcherAssert.assertThat(comment, Matchers.is(Matchers.not(new Comment(id, createdBy, createdAt, updatedAt, body, "String-ad3be5cf-83bf-4b6f-980b-426b0acb2633"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(comment.hashCode(), Matchers.is(new Comment(id, createdBy, createdAt, updatedAt, body, renderedBody).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(comment.toString(), Matchers.startsWith("Comment"));
    }

}

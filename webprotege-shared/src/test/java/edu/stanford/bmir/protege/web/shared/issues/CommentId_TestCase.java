
package edu.stanford.bmir.protege.web.shared.issues;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(MockitoJUnitRunner.class)
public class CommentId_TestCase {

    private CommentId commentId;

    @Before
    public void setUp() {
        commentId = CommentId.create();
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(commentId, is(commentId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(commentId.equals(null), is(false));
    }

    @Test
    public void shouldNotBeEqualToOther() {
        assertThat(commentId, is(not(CommentId.create())));
    }


    @Test
    public void shouldNotBeEqualToOtherHashCode() {
        assertThat(commentId.hashCode(), is(not(CommentId.create().hashCode())));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(commentId.toString(), Matchers.startsWith("CommentId"));
    }
}

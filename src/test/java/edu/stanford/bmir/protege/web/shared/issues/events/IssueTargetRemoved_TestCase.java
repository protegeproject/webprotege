
package edu.stanford.bmir.protege.web.shared.issues.events;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class IssueTargetRemoved_TestCase {

    private IssueTargetRemoved issueTargetRemoved;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Mock
    private OWLEntity entity;

    @Before
    public void setUp()
            throws Exception {
        issueTargetRemoved = new IssueTargetRemoved(userId, timestamp, entity);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueTargetRemoved(null, timestamp, entity);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueTargetRemoved.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueTargetRemoved.getTimestamp(), is(this.timestamp));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new IssueTargetRemoved(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(issueTargetRemoved.getEntity(), is(this.entity));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueTargetRemoved, is(issueTargetRemoved));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(issueTargetRemoved.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueTargetRemoved, is(new IssueTargetRemoved(userId, timestamp, entity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueTargetRemoved,
                   is(not(new IssueTargetRemoved(Mockito.mock(UserId.class), timestamp, entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueTargetRemoved, is(not(new IssueTargetRemoved(userId, 2L, entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(issueTargetRemoved,
                   is(not(new IssueTargetRemoved(userId, timestamp, Mockito.mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueTargetRemoved.hashCode(),
                   is(new IssueTargetRemoved(userId, timestamp, entity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueTargetRemoved.toString(), startsWith("IssueEntityTargetRemoved" ));
    }

}

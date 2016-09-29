
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
public class IssueTargetAdded_TestCase {

    private IssueTargetAdded issueTargetAdded;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Mock
    private OWLEntity targetEntity;

    @Before
    public void setUp()
            throws Exception {
        issueTargetAdded = new IssueTargetAdded(userId, timestamp, targetEntity);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueTargetAdded(null, timestamp, targetEntity);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueTargetAdded.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueTargetAdded.getTimestamp(), is(this.timestamp));
    }

    @SuppressWarnings("ConstantConditions" )
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_targetEntity_IsNull() {
        new IssueTargetAdded(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_targetEntity() {
        assertThat(issueTargetAdded.getTargetEntity(), is(this.targetEntity));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueTargetAdded, is(issueTargetAdded));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(issueTargetAdded.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueTargetAdded, is(new IssueTargetAdded(userId, timestamp, targetEntity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueTargetAdded,
                   is(not(new IssueTargetAdded(Mockito.mock(UserId.class), timestamp, targetEntity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueTargetAdded, is(not(new IssueTargetAdded(userId, 2L, targetEntity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_targetEntity() {
        assertThat(issueTargetAdded,
                   is(not(new IssueTargetAdded(userId, timestamp, Mockito.mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueTargetAdded.hashCode(),
                   is(new IssueTargetAdded(userId, timestamp, targetEntity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueTargetAdded.toString(), startsWith("IssueEntityTargetAdded" ));
    }

}


package edu.stanford.bmir.protege.web.server.owlapi.change;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class Revision_TestCase {

    private Revision revision;

    @Mock
    private UserId userId;

    @Mock
    private RevisionNumber revisionNumber;

    @Mock
    private ImmutableList<OWLOntologyChangeRecord> changes;

    private long timestamp = 1L;

    private String highLevelDescription = "The highLevelDescription";

    @Before
    public void setUp() throws Exception {
        revision = new Revision(userId, revisionNumber, changes, timestamp, highLevelDescription);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new Revision(null, revisionNumber, changes, timestamp, highLevelDescription);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        MatcherAssert.assertThat(revision.getUserId(), Matchers.is(this.userId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_revisionNumber_IsNull() {
        new Revision(userId, null, changes, timestamp, highLevelDescription);
    }

    @Test
    public void shouldReturnSupplied_revisionNumber() {
        MatcherAssert.assertThat(revision.getRevisionNumber(), Matchers.is(this.revisionNumber));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_changes_IsNull() {
        new Revision(userId, revisionNumber, null, timestamp, highLevelDescription);
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        MatcherAssert.assertThat(revision.getTimestamp(), Matchers.is(this.timestamp));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_highLevelDescription_IsNull() {
        new Revision(userId, revisionNumber, changes, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_highLevelDescription() {
        MatcherAssert.assertThat(revision.getHighLevelDescription(), Matchers.is(this.highLevelDescription));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(revision, Matchers.is(revision));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(revision.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(revision, Matchers.is(new Revision(userId, revisionNumber, changes, timestamp, highLevelDescription)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(revision, Matchers.is(Matchers.not(new Revision(mock(UserId.class), revisionNumber, changes, timestamp, highLevelDescription))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_revisionNumber() {
        MatcherAssert.assertThat(revision, Matchers.is(Matchers.not(new Revision(userId, mock(RevisionNumber.class), changes, timestamp, highLevelDescription))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_changes() {
        MatcherAssert.assertThat(revision, Matchers.is(Matchers.not(new Revision(userId, revisionNumber, mock(ImmutableList.class), timestamp, highLevelDescription))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        MatcherAssert.assertThat(revision, Matchers.is(Matchers.not(new Revision(userId, revisionNumber, changes, 2L, highLevelDescription))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_highLevelDescription() {
        MatcherAssert.assertThat(revision, Matchers.is(Matchers.not(new Revision(userId, revisionNumber, changes, timestamp, "String-da7e91b0-376b-4bdf-903a-61d17ea23378"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(revision.hashCode(), Matchers.is(new Revision(userId, revisionNumber, changes, timestamp, highLevelDescription).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(revision.toString(), Matchers.startsWith("Revision"));
    }

    @Test
    public void should_getSize() {
        when(changes.size()).thenReturn(3);
        MatcherAssert.assertThat(revision.getSize(), Matchers.is(3));
    }

}

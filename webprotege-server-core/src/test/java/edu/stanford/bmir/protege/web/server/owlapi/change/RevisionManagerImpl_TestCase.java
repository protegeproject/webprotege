
package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManagerImpl;
import edu.stanford.bmir.protege.web.server.revision.RevisionStore;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RevisionManagerImpl_TestCase {

    public static final String HIGHLEVEL_DESC = "HIGHLEVEL_DESC";

    public static final long TIME_STAMP = 33l;

    private RevisionManagerImpl manager;

    @Mock
    private RevisionStore revisionStore;

    @Mock
    private UserId userId;

    @Mock
    private Revision revision;

    @Mock
    private RevisionNumber revisionNumber, nextRevisionNumber;

    @Mock
    private ImmutableList<OWLOntologyChangeRecord> changes;

    @Before
    public void setUp() throws Exception {
        manager = new RevisionManagerImpl(revisionStore);
        when(revisionStore.getCurrentRevisionNumber()).thenReturn(revisionNumber);
        when(revisionStore.getRevision(revisionNumber)).thenReturn(java.util.Optional.of(revision));
        when(revisionStore.getRevisions()).thenReturn(ImmutableList.of(revision));
        when(revisionNumber.getNextRevisionNumber()).thenReturn(nextRevisionNumber);
        when(revision.getUserId()).thenReturn(userId);
        when(revision.getChanges()).thenReturn(changes);
        when(revision.getHighLevelDescription()).thenReturn(HIGHLEVEL_DESC);
        when(revision.getSize()).thenReturn(1);
        when(revision.getTimestamp()).thenReturn(TIME_STAMP);
        when(revision.getRevisionNumber()).thenReturn(revisionNumber);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_revisionStore_IsNull() {
        new RevisionManagerImpl(null);
    }

    @Test
    public void should_getCurrentRevision() {
        assertThat(manager.getCurrentRevision(), is(revisionNumber));
    }

    @Test
    public void should_getRevisions() {
        assertThat(manager.getRevisions(), is(Collections.singletonList(revision)));
    }

    @Test
    public void should_getRevision() {
        assertThat(manager.getRevision(revisionNumber), is(Optional.of(revision)));
    }

    @Test
    public void should_getRevisionSummary() {
        java.util.Optional<RevisionSummary> summary = manager.getRevisionSummary(revisionNumber);
        assertThat(summary.isPresent(), is(true));
    }

    @Test
    public void should_getRevisionSummaryWithCorrectUserId() {
        java.util.Optional<RevisionSummary> summary = manager.getRevisionSummary(revisionNumber);
        RevisionSummary revisionSummary = summary.get();
        assertThat(revisionSummary.getUserId(), is(userId));
    }

    @Test
    public void should_getRevisionSummaryWithCorrectRevisionNumber() {
        java.util.Optional<RevisionSummary> summary = manager.getRevisionSummary(revisionNumber);
        RevisionSummary revisionSummary = summary.get();
        assertThat(revisionSummary.getRevisionNumber(), is(revisionNumber));
    }

    @Test
    public void should_getRevisionSummaryWithCorrectChangeCount() {
        java.util.Optional<RevisionSummary> summary = manager.getRevisionSummary(revisionNumber);
        RevisionSummary revisionSummary = summary.get();
        assertThat(revisionSummary.getChangeCount(), is(1));
    }

    @Test
    public void should_getRevisionSummaryWithCorrectTimestamp() {
        java.util.Optional<RevisionSummary> summary = manager.getRevisionSummary(revisionNumber);
        RevisionSummary revisionSummary = summary.get();
        assertThat(revisionSummary.getTimestamp(), is(TIME_STAMP));
    }

    @Test
    public void should_getRevisionSummaries() {
        assertThat(manager.getRevisionSummaries(), hasSize(1));
    }


    @Test
    public void should_addRevision() {
        UserId userId = mock(UserId.class);
        List<OWLOntologyChangeRecord> changes = Arrays.asList(new OWLOntologyChangeRecord(new OWLOntologyID(), new AddAxiomData(mock(OWLAxiom.class))));
        manager.addRevision(userId, changes, HIGHLEVEL_DESC);
        ArgumentCaptor<Revision> revisionCaptor = ArgumentCaptor.forClass(Revision.class);
        verify(revisionStore, times(1)).addRevision(revisionCaptor.capture());
        Revision addedRevision = revisionCaptor.getValue();
        assertThat(addedRevision.getUserId(), is(userId));
        assertThat(addedRevision.getHighLevelDescription(), is(HIGHLEVEL_DESC));
        assertThat(addedRevision.getRevisionNumber(), is(nextRevisionNumber));
    }
}

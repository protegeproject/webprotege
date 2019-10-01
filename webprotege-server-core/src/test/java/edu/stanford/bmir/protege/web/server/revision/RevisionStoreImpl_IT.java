package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeRecordTranslatorImpl;
import edu.stanford.bmir.protege.web.server.inject.ChangeHistoryFileFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class RevisionStoreImpl_IT {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private RevisionStoreImpl store;

    private OWLOntologyID ontologyId;

    private OWLAxiom axiom;

    private File changeHistoryFile;

    private ProjectId projectId;

    private OWLDataFactoryImpl dataFactory;

    private OntologyChangeRecordTranslatorImpl changeRecordTranslator;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Mock
    private ChangeHistoryFileFactory changeHistoryFileFactory;

    @Before
    public void setUp() throws IOException {
        temporaryFolder.create();
        projectId = ProjectId.get(UUID.randomUUID()
                                          .toString());
        changeHistoryFile = temporaryFolder.newFile();
        when(changeHistoryFileFactory.getChangeHistoryFile(projectId))
                .thenReturn(changeHistoryFile);
        dataFactory = new OWLDataFactoryImpl();
        changeRecordTranslator = new OntologyChangeRecordTranslatorImpl();

        ontologyId = new OWLOntologyID(IRI.create("http://example.org/OntA"));
        var clsA = dataFactory.getOWLClass(IRI.create("http://example.org/A"));
        var clsB = dataFactory.getOWLClass(IRI.create("http://example.org/A"));
        axiom = SubClassOf(clsA, clsB);

        store = new RevisionStoreImpl(projectId,
                                      changeHistoryFileFactory,
                                      dataFactory,
                                      changeRecordTranslator);
    }

    @Test
    public void shouldAddRevision() {
        var revision = createRevision();
        store.addRevision(revision);
        var revisions = store.getRevisions();
        assertThat(revisions, contains(revision));
    }

    @Test
    public void shouldHaveZeroRevisionNumberAtStart() {
        var revisionNumber = store.getCurrentRevisionNumber();
        assertThat(revisionNumber.getValue(), is(0L));
    }

    @Test
    public void shouldIncrementCurrentRevisionNumber() {
        var revision = createRevision();
        store.addRevision(revision);
        var revisionNumber = store.getCurrentRevisionNumber();
        assertThat(revisionNumber.getValue(), is(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfAddedRevisionNumberIsEqualToCurrentRevisionNumber() {
        var revision = createRevision();
        store.addRevision(revision);
        store.addRevision(revision);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfAddedRevisionNumberIsLessThanCurrentRevisionNumber() {
        var revision = createRevision();
        store.addRevision(revision);
        var smallerRevision = createRevision(RevisionNumber.getRevisionNumber(0));
        store.addRevision(smallerRevision);
    }

    @Test
    public void shouldSaveFirstRevisionImmediately() {
        var revision = createRevision();
        assertThat(changeHistoryFile.length(), is(0L));
        store.addRevision(revision);
        assertThat(changeHistoryFile.length(), is(greaterThan(0L)));
    }

    @Test
    public synchronized void shouldSaveSubsequentRevisions() throws InterruptedException {
        store.addRevision(createRevision(RevisionNumber.getRevisionNumber(1)));
        var initialLength = changeHistoryFile.length();
        // Add hook to be notified of when the save has taken place
        store.setSavedHook(() -> countDownLatch.countDown());
        store.addRevision(createRevision(RevisionNumber.getRevisionNumber(2)));
        // Wait until it has been saved
        countDownLatch.await();
        var nextLength = changeHistoryFile.length();
        assertThat(nextLength, is(greaterThan(initialLength)));
    }

    @Test
    public void shouldGetRevision() {
        var revision = createRevision();
        store.addRevision(revision);
        var retrievedRevision = store.getRevision(RevisionNumber.getRevisionNumber(1));
        assertThat(retrievedRevision, is(equalTo(Optional.of(revision))));
    }

    @Test
    public void shouldNotGetRevision() {
        var revision = store.getRevision(RevisionNumber.getRevisionNumber(1));
        assertThat(revision.isEmpty(), is(true));
    }

    @Test
    public void shouldLoadSavedRevision() {
        var revision = createRevision();
        store.addRevision(revision);
        var otherStore = new RevisionStoreImpl(projectId, changeHistoryFileFactory, dataFactory, changeRecordTranslator);
        otherStore.load();
        var revisions = store.getRevisions();
        assertThat(revisions, contains(revision));
        otherStore.dispose();
    }

    private Revision createRevision() {
        var revisionNumber = RevisionNumber.getRevisionNumber(1);
        return createRevision(revisionNumber);
    }

    private Revision createRevision(RevisionNumber revisionNumber) {
        var changes = ImmutableList.<OntologyChange>of(
                AddAxiomChange.of(ontologyId, axiom)
        );
        var userId = UserId.getUserId("The User");
        var timestamp = System.currentTimeMillis();
        var highLevelDescription = "A change that was mad";
        return new Revision(userId,
                                         revisionNumber,
                                         changes,
                                         timestamp,
                                         highLevelDescription);
    }

    @After
    public void tearDown() throws Exception {
        store.dispose();
    }
}

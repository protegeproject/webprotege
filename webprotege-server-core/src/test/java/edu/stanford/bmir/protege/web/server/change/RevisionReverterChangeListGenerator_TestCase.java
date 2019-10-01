package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.image.renderable.RenderContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class RevisionReverterChangeListGenerator_TestCase {

    private RevisionReverterChangeListGenerator generator;

    private RevisionNumber revisionNumber = RevisionNumber.getRevisionNumber(3);

    @Mock
    private RevisionManager revisionManager;

    @Mock
    private Revision revision;

    @Mock
    private ChangeGenerationContext context;

    @Mock
    private OntologyChange changeO, inverseChange0, change1, inverseChange1;

    @Before
    public void setUp() {
        var changes = ImmutableList.of(changeO, change1);
        generator = new RevisionReverterChangeListGenerator(revisionNumber,
                                                            revisionManager);
        when(revisionManager.getRevision(revisionNumber))
                .thenReturn(Optional.of(revision));

        when(revision.getChanges())
                .thenReturn(changes);

        when(changeO.getInverseChange())
                .thenReturn(inverseChange0);
        when(change1.getInverseChange())
                .thenReturn(inverseChange1);
    }

    @Test
    public void shouldGenerateInverseChangesInReverseOrder() {
        var ontologyChangeList = generator.generateChanges(context);
        var inverseChanges = ontologyChangeList.getChanges();
        assertThat(inverseChanges, contains(inverseChange1, inverseChange0));
    }

    @Test
    public void shouldGenerateEmptyListForUnknownRevision() {
        when(revisionManager.getRevision(revisionNumber))
                .thenReturn(Optional.empty());
        var ontologyChangeList = generator.generateChanges(context);
        var inverseChanges = ontologyChangeList.getChanges();
        assertThat(inverseChanges, is(empty()));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetMessageContainingRevisionNumber() {
        var message = generator.getMessage(mock(ChangeApplicationResult.class));
        assertThat(message, Matchers.containsString(" 3"));
    }

    @Test
    public void shouldGetRenamedResult() {
        var renameMap = mock(RenameMap.class);
        var result = generator.getRenamedResult(true, renameMap);
        assertThat(result, is(true));
    }
}

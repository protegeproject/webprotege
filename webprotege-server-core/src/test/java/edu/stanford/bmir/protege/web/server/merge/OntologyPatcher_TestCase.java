package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.diff.OntologyDiff2OntologyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyPatcher_TestCase {

    private OntologyPatcher patcher;

    @Mock
    private HasApplyChanges changeManager;

    @Mock
    private OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges;

    @Mock
    private OntologyDiff ontologyDiff;

    private String commitMessage = "The Commit Message";

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private OntologyChange ontologyChange;

    @Mock
    private UserId userId;

    @Captor
    private ArgumentCaptor<UserId> userIdCaptor;


    @Captor
    private ArgumentCaptor<ChangeListGenerator<?>> changeListGeneratorCaptor;

    @Before
    public void setUp() {
        patcher = new OntologyPatcher(changeManager,
                                      ontologyDiff2OntologyChanges);
        when(ontologyDiff2OntologyChanges.getOntologyChangesFromDiff(ontologyDiff))
                .thenReturn(ImmutableList.of(ontologyChange));
        when(executionContext.getUserId())
                .thenReturn(userId);
    }

    @Test
    public void shouldApplyChangesWithExecContextUser() {
        patcher.applyPatch(Collections.singleton(ontologyDiff),
                           commitMessage,
                           executionContext);

        verify(changeManager, times(1)).applyChanges(userIdCaptor.capture(), changeListGeneratorCaptor.capture());
        assertThat(userIdCaptor.getValue(), is(userId));
    }

    @Test
    public void shouldCreateChangeGeneratorForOntologyChanges() {
        patcher.applyPatch(Collections.singleton(ontologyDiff),
                           commitMessage,
                           executionContext);

        verify(changeManager, times(1)).applyChanges(userIdCaptor.capture(), changeListGeneratorCaptor.capture());

        var changeListGenerator = changeListGeneratorCaptor.getValue();
        var changes = changeListGenerator.generateChanges(new ChangeGenerationContext(userId));
        assertThat(changes.getChanges(), hasItem(ontologyChange));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateChangeGeneratorWithSuppliedMessage() {
        patcher.applyPatch(Collections.singleton(ontologyDiff),
                           commitMessage,
                           executionContext);

        verify(changeManager, times(1)).applyChanges(userIdCaptor.capture(), changeListGeneratorCaptor.capture());

        var changeListGenerator = changeListGeneratorCaptor.getValue();
        var message = changeListGenerator.getMessage(mock(ChangeApplicationResult.class));
        assertThat(message, is(this.commitMessage));


    }
}

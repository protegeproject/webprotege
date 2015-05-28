package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.WatchedChangesManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetWatchedEntitiesActionHandler_TestCase {

    private GetWatchedEntityChangesActionHandler handler;

    @Mock
    private GetWatchedEntityChangesAction action;

    @Mock
    private OWLAPIProject project;

    @Mock
    private ExecutionContext context;

    @Mock
    private WatchedChangesManager watchedChangesManager;

    @Mock
    private WatchManager watchManager;

    @Mock
    private UserId userId;

    @Mock
    private Set<Watch<?>> watches;

    @Mock
    private ImmutableList<ProjectChange> projectChanges;

    @Mock
    private OWLAPIProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        handler = new GetWatchedEntityChangesActionHandler(projectManager);
        when(action.getUserId()).thenReturn(userId);
        when(project.getWatchManager()).thenReturn(watchManager);
        when(watchManager.getWatches(userId)).thenReturn(watches);
        when(project.getWatchedChangesManager()).thenReturn(watchedChangesManager);
        when(watchedChangesManager.getProjectChangesForWatches(watches)).thenReturn(projectChanges);
    }

    @Test
    public void shouldGetChanges() {
        GetWatchedEntityChangesResult result = handler.execute(action, project, context);
        assertThat(result.getChanges(), is(projectChanges));
    }
}

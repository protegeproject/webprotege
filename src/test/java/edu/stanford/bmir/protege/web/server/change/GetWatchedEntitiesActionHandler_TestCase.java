package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.server.watches.WatchedChangesManager;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    private Project project;

    @Mock
    private ExecutionContext context;

    @Mock
    private WatchedChangesManager watchedChangesManager;

    @Mock
    private WatchManager watchManager;

    @Mock
    private UserId userId;

    @Mock
    private Set<Watch> watches;

    @Mock
    private ImmutableList<ProjectChange> projectChanges;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        handler = new GetWatchedEntityChangesActionHandler(accessManager, watchManager, watchedChangesManager);
        when(action.getUserId()).thenReturn(userId);
        when(project.getWatchManager()).thenReturn(watchManager);
        when(watchManager.getWatches(userId)).thenReturn(watches);
        when(project.getWatchedChangesManager()).thenReturn(watchedChangesManager);
        when(watchedChangesManager.getProjectChangesForWatches(watches)).thenReturn(projectChanges);
    }

    @Test
    public void shouldGetChanges() {
        GetWatchedEntityChangesResult result = handler.execute(action, context);
        assertThat(result.getChanges(), is(projectChanges));
    }
}

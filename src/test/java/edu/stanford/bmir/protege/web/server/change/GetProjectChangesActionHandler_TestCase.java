package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.ProjectChangesManager;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectChangesActionHandler_TestCase {

    private GetProjectChangesActionHandler handler;

    @Mock
    private GetProjectChangesAction action;

    @Mock
    private OWLAPIProject project;

    @Mock
    private ProjectChangesManager changeManager;

    @Mock
    private ExecutionContext context;

    private Optional<OWLEntity> subject = Optional.of(mock(OWLEntity.class));

    @Mock
    private ImmutableList<ProjectChange> projectChanges;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        when(action.getSubject()).thenReturn(subject);
        handler = new GetProjectChangesActionHandler(projectManager, accessManager);
        when(project.getProjectChangesManager()).thenReturn(changeManager);
        when(changeManager.getProjectChanges(subject)).thenReturn(projectChanges);
    }

    @Test
    public void shouldGetProjectChangesFromManager() {
        GetProjectChangesResult result = handler.execute(action, project, context);
        assertThat(result.getChanges(), is(projectChanges));
    }
}

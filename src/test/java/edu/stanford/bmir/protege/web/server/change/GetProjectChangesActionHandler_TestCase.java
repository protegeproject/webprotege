package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
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
import static org.hamcrest.Matchers.*;
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
    private OWLAPIChangeManager changeManager;

    @Mock
    private ExecutionContext context;

    private Optional<OWLEntity> subject = Optional.of(mock(OWLEntity.class));

    @Mock
    private ImmutableList<ProjectChange> projectChanges;

    @Before
    public void setUp() throws Exception {
        when(action.getSubject()).thenReturn(subject);
        handler = new GetProjectChangesActionHandler();
        when(project.getChangeManager()).thenReturn(changeManager);
        when(changeManager.getProjectChanges(subject)).thenReturn(projectChanges);
    }

    @Test
    public void shouldGetProjectChangesFromManager() {
        GetProjectChangesResult result = handler.execute(action, project, context);
        assertThat(result.getChanges(), is(projectChanges));
    }
}

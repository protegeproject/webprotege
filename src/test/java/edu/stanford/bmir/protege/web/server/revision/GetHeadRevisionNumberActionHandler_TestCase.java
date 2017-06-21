package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetHeadRevisionNumberActionHandler_TestCase {

    private GetHeadRevisionNumberActionHandler handler;

    @Mock
    private GetHeadRevisionNumberAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private RevisionNumber revisionNumber;

    @Mock
    private RevisionManager revisionManager;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        handler = new GetHeadRevisionNumberActionHandler(accessManager, revisionManager);
    }

    @Test
    public void shouldReturnProjectRevision() {
        GetHeadRevisionNumberResult result = handler.execute(action, executionContext);
        assertThat(result.getRevisionNumber(), is(revisionNumber));
    }
}

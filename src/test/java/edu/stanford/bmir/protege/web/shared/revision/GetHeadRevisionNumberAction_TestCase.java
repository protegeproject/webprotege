package edu.stanford.bmir.protege.web.shared.revision;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetHeadRevisionNumberAction_TestCase {


    private GetHeadRevisionNumberAction action;

    private GetHeadRevisionNumberAction otherAction;

    @Mock
    private ProjectId projectId;


    @Before
    public void setUp() throws Exception {
        action = new GetHeadRevisionNumberAction(projectId);
        otherAction = new GetHeadRevisionNumberAction(projectId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetHeadRevisionNumberAction(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(equalTo(action)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(action, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(equalTo(otherAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(action.hashCode(), is(otherAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(action.toString(), startsWith("GetHeadRevisionNumberAction"));
    }

    @Test
    public void shouldReturnSupplied_ProjectId() {
        assertThat(action.getProjectId(), is(projectId));
    }
}
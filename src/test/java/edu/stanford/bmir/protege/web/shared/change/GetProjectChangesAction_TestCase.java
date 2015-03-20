package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectChangesAction_TestCase {


    private GetProjectChangesAction action;

    private GetProjectChangesAction otherAction;

    @Mock
    private ProjectId projectId;

    @Mock
    private Optional<OWLEntity> subject;


    @Before
    public void setUp() throws Exception {
        action = new GetProjectChangesAction(projectId, subject);
        otherAction = new GetProjectChangesAction(projectId, subject);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
        new GetProjectChangesAction(null, subject);
    }


    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_Subject_IsNull() {
        new GetProjectChangesAction(projectId, null);
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
        assertThat(action.toString(), startsWith("GetProjectChangesAction"));
    }

    @Test
    public void shouldReturnSuppliedProjectId() {
        assertThat(action.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnSuppliedSubject() {
        assertThat(action.getSubject(), is(subject));
    }
}
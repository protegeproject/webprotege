package edu.stanford.bmir.protege.web.shared.metrics;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class GetMetricsActionTestCase {

    @Mock
    private ProjectId projectId;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullProjectId() {
        new GetMetricsAction(null);
    }

    @Test
    public void shouldReturnSuppliedProjectId() {
        GetMetricsAction action = new GetMetricsAction(projectId);
        assertThat(action.getProjectId(), is(projectId));
    }

    @Test
    public void shouldBeEqualForEqualProjectIds() {
        GetMetricsAction actionA = new GetMetricsAction(projectId);
        GetMetricsAction actionB = new GetMetricsAction(projectId);
        assertThat(actionA, is(equalTo(actionB)));
    }

    @Test
    public void shouldHaveSameHashCodeForEqualProjectIds() {
        GetMetricsAction actionA = new GetMetricsAction(projectId);
        GetMetricsAction actionB = new GetMetricsAction(projectId);
        assertThat(actionA.hashCode(), is(equalTo(actionB.hashCode())));
    }

}

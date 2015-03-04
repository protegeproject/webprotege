package edu.stanford.bmir.protege.web.server.metrics;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsResult;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class GetMetricsActionHandlerTestCase {

    @Mock
    protected ProjectId projectId;

    @Mock
    protected OWLAPIProject project;

    @Mock
    protected OWLAPIProjectMetricsManager metricsManager;

    @Mock
    protected ImmutableList<MetricValue> metricValues;

    @Mock
    private OWLAPIProjectManager projectManager;


    @Before
    public void setUp() {
        when(project.getMetricsManager()).thenReturn(metricsManager);
        when(metricsManager.getMetrics()).thenReturn(metricValues);
    }

    @Test
    public void shouldReturnMetricValues() {
        GetMetricsActionHandler handler = new GetMetricsActionHandler(projectManager);
        GetMetricsResult result = handler.execute(new GetMetricsAction(projectId), project, mock(ExecutionContext.class));
        assertThat(result.getMetricValues(), is(equalTo(metricValues)));
    }

}

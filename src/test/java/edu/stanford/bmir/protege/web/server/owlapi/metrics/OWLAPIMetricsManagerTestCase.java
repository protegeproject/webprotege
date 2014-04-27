package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import com.beust.jcommander.internal.Lists;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class OWLAPIMetricsManagerTestCase {

    @Mock
    protected ProjectId projectId;

    @Mock
    protected MetricCalculator metric;

    @Mock
    protected MetricValue metricValue;

    @Mock
    protected MetricValue metricValue2;

    @Mock
    protected List<OWLOntologyChange> changes;

    @Mock
    protected WebProtegeLogger logger;

    @Mock
    protected HasPostEvents<ProjectEvent<?>> eventBus;

    private OWLAPIProjectMetricsManager metricsManager;


    @Before
    public void setUp() throws Exception {
        List<MetricCalculator> metricList = Lists.newArrayList();
        metricList.add(metric);
        metricsManager = new OWLAPIProjectMetricsManager(projectId, metricList, eventBus, logger);
    }

    @Test
    public void shouldGetMetrics() {
        when(metric.computeValue()).thenReturn(metricValue);
        List<MetricValue> values = metricsManager.getMetrics();
        assertThat(values, hasItem(metricValue));
    }

    @Test
    public void shouldReturnCachedValue() {
        when(metric.computeValue()).thenReturn(metricValue).thenReturn(metricValue2);
        List<MetricValue> values = metricsManager.getMetrics();
        assertThat(values, hasItem(metricValue));
        List<MetricValue> values2 = metricsManager.getMetrics();
        assertThat(values2, hasItem(metricValue));
        verify(metric, times(1)).computeValue();
    }

    @Test
    public void shouldFireEventAfterChanges() {
        when(metric.computeValue()).thenReturn(metricValue).thenReturn(metricValue2);
        when(metric.getStateAfterChanges(changes)).thenReturn(OWLAPIProjectMetricState.DIRTY);
        metricsManager.handleOntologyChanges(changes);
        verify(eventBus, times(1)).postEvent(any(ProjectEvent.class));
    }

    @Test
    public void shouldReturnFreshValueAfterChanges() {
        when(metric.computeValue()).thenReturn(metricValue).thenReturn(metricValue2);
        when(metric.getStateAfterChanges(changes)).thenReturn(OWLAPIProjectMetricState.DIRTY);

        List<MetricValue> values = metricsManager.getMetrics();
        assertThat(values, hasItem(metricValue));

        metricsManager.handleOntologyChanges(changes);
        List<MetricValue> values2 = metricsManager.getMetrics();
        // Should have fresh value
        assertThat(values2, hasItem(metricValue2));
        verify(metric, times(2)).computeValue();
    }

    @Test
    public void shouldHandleExceptionThrownDuringCompute() {
        RuntimeException exception = mock(RuntimeException.class);
        when(metric.computeValue()).thenThrow(exception);
        List<MetricValue> values = metricsManager.getMetrics();
        assertThat(values.isEmpty(), is(true));
        // Make sure that the exception is logged.
        verify(logger, times(1)).severe(exception);
    }
}

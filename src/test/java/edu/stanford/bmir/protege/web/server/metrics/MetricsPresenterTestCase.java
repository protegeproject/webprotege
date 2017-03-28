package edu.stanford.bmir.protege.web.server.metrics;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.RequestRefreshEvent;
import edu.stanford.bmir.protege.web.client.events.RequestRefreshEventHandler;
import edu.stanford.bmir.protege.web.client.metrics.MetricsPresenter;
import edu.stanford.bmir.protege.web.client.metrics.MetricsView;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.metrics.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricsPresenterTestCase {

    @Mock
    ProjectId projectId;

    @Mock
    private DispatchServiceManager dispatchServiceManager;

    @Mock
    protected DispatchServiceCallback<GetMetricsResult> callback;

    @Mock
    protected GetMetricsResult result;

    @Mock
    protected MetricsView view;

    @Mock
    protected WebProtegeEventBus eventManager;

    @Mock
    protected ImmutableList<MetricValue> metricValues;

    private RequestRefreshEventHandler handler;

    @Mock
    private MetricsChangedHandler metricsChangedHandler;

    private MetricsPresenter presenter;

    @Before
    public void setUp() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                DispatchServiceCallback<GetMetricsResult> action = (DispatchServiceCallback<GetMetricsResult>) invocationOnMock.getArguments()[1];
                action.onSuccess(result);
                return null;
            }
        }).when(dispatchServiceManager).execute(any(GetMetricsAction.class), any(DispatchServiceCallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                handler = (RequestRefreshEventHandler) invocationOnMock.getArguments()[0];
                return null;
            }
        }).when(view).setRequestRefreshEventHandler(any(RequestRefreshEventHandler.class));
        when(result.getMetricValues()).thenReturn(metricValues);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                metricsChangedHandler = (MetricsChangedHandler) invocationOnMock.getArguments()[0];
                return null;
            }
        }).when(eventManager).addProjectEventHandler(eq(projectId), any(MetricsChangedEvent.getType().getClass()), any(MetricsChangedHandler.class));

        presenter = new MetricsPresenter(projectId, view, dispatchServiceManager);
        presenter.start();
    }


    @Test
    public void shouldGetMetricsOnRequestRefresh() {
        handler.handleRequestRefresh(mock(RequestRefreshEvent.class));
        verify(view, atLeastOnce()).setMetrics(metricValues);
    }

    @Test
    public void shouldGetMetricsOnReload() {
        presenter.handleMetricsChanged();
        verify(view).setMetrics(metricValues);
    }
}

package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationExecutorsRegistry_TestCase {

    private ApplicationExecutorsRegistry registry;

    @Mock
    private ApplicationDisposablesManager applicationDisposablesManager;

    @Mock
    private ExecutorService executorService;

    private final String serviceName = "The service name";

    @Before
    public void setUp() throws Exception {
        registry = new ApplicationExecutorsRegistry(applicationDisposablesManager);
    }

    @Test
    public void shouldRegisterService() {
        registry.registerService(executorService, serviceName);
        var shutdownTask = ArgumentCaptor.forClass(ExecutorServiceShutdownTask.class);
        verify(applicationDisposablesManager, times(1)).register(shutdownTask.capture());
        var service = shutdownTask.getValue().getExecutorService();
        assertThat(service, is(executorService));
    }

    @Test
    public void shouldNotRegisterMultipleTimes() {
        registry.registerService(executorService, serviceName);
        registry.registerService(executorService, serviceName);
        verify(applicationDisposablesManager, times(1)).register(any());
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfServiceIsNull() {
        registry.registerService(null, serviceName);
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfServiceNameIsNull() {
        registry.registerService(executorService, null);
    }
}
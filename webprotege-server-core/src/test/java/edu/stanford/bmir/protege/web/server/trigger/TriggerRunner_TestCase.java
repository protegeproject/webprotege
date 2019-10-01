package edu.stanford.bmir.protege.web.server.trigger;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.EntityFrameMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class TriggerRunner_TestCase<C> {

    private TriggerRunner runner;

    @Mock
    private Trigger triggerA, triggerB;

    @Mock
    private EntityFrameMatcher matcherA, matcherB;

    @Mock
    private TriggerAction<C> actionA1, actionA2, actionB1, actionB2;

    @Mock
    private C contextA1, contextA2, contextB1, contextB2;

    @Mock
    private OWLEntity entity1, entity2;

    private Stream<OWLEntity> stream;

    @Before
    public void setUp() {
        runner = new TriggerRunner(ImmutableList.of(triggerA, triggerB));
        when(triggerA.getMatcher()).thenReturn(matcherA);
        when(triggerB.getMatcher()).thenReturn(matcherB);
        when(triggerA.getTriggerActions()).thenReturn(ImmutableList.of(actionA1, actionA2));
        when(triggerB.getTriggerActions()).thenReturn(ImmutableList.of(actionB1, actionB2));
        when(actionA1.begin()).thenReturn(contextA1);
        when(actionA2.begin()).thenReturn(contextA2);
        when(actionB1.begin()).thenReturn(contextB1);
        when(actionB2.begin()).thenReturn(contextB2);

        stream = Stream.of(entity1, entity2);
    }

    @Test
    public void shouldCallBeginForAllActions() {
        runner.execute(stream);
        verify(actionA1, times(1)).begin();
        verify(actionA2, times(1)).begin();
        verify(actionB1, times(1)).begin();
        verify(actionB2, times(1)).begin();
    }

    @Test
    public void shouldNotExecuteUnmatchedTriggers() {
        runner.execute(stream);
        verify(actionA1, never()).execute(any(), any());
        verify(actionA2, never()).execute(any(), any());
        verify(actionB1, never()).execute(any(), any());
        verify(actionB2, never()).execute(any(), any());
    }

    @Test
    public void shouldExecuteMatchedTriggersOnly() {
        when(matcherA.matches(entity1)).thenReturn(true);
        runner.execute(stream);
        verify(actionA1, times(1)).execute(entity1, contextA1);
        verify(actionA2, times(1)).execute(entity1, contextA2);
        verify(actionB1, never()).execute(any(), any());
        verify(actionB2, never()).execute(any(), any());
    }

    @Test
    public void shouldCallEndForAllActions() {
        runner.execute(stream);
        verify(actionA1, times(1)).end(contextA1);
        verify(actionA2, times(1)).end(contextA2);
        verify(actionB1, times(1)).end(contextB1);
        verify(actionB2, times(1)).end(contextB2);
    }
}

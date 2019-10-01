package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.impl.AxiomChangeHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomChangeHandler_TestCase {

    private AxiomChangeHandler handler;

    @Mock
    private Consumer<AddAxiomChange> addAxiomChangeConsumer;

    @Mock
    private Consumer<RemoveAxiomChange> removeAxiomChangeConsumer;

    @Mock
    private Consumer<AxiomChange> axiomChangeConsumer;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    private AddAxiomChange addAxiomChange;

    private RemoveAxiomChange removeAxiomChange;

    @Before
    public void setUp() {
        addAxiomChange = AddAxiomChange.of(ontologyId, axiom);
        removeAxiomChange = RemoveAxiomChange.of(ontologyId, axiom);
        handler = new AxiomChangeHandler();
        handler.setAddAxiomChangeConsumer(addAxiomChangeConsumer);
        handler.setRemoveAxiomChangeConsumer(removeAxiomChangeConsumer);
        handler.setAxiomChangeConsumer(axiomChangeConsumer);
    }

    @Test
    public void shouldCallAxiomChangeConsumerOnAddAxiom() {
        handler.handleOntologyChanges(List.of(addAxiomChange));
        verify(axiomChangeConsumer, times(1)).accept(addAxiomChange);
    }

    @Test
    public void shouldCallAddAxiomChangeConsumerOnAddAxiom() {
        handler.handleOntologyChanges(List.of(addAxiomChange));
        verify(addAxiomChangeConsumer, times(1)).accept(addAxiomChange);
    }

    @Test
    public void shouldNotCallRemoveAxiomChangeConsumerOnAddAxiom() {
        handler.handleOntologyChanges(List.of(addAxiomChange));
        verify(removeAxiomChangeConsumer, never()).accept(any());
    }

    @Test
    public void shouldCallAxiomChangeConsumerOnRemoveAxiom() {
        handler.handleOntologyChanges(List.of(removeAxiomChange));
        verify(axiomChangeConsumer, times(1)).accept(any());
    }

    @Test
    public void shouldCallRemoveAxiomChangeConsumerOnRemoveAxiom() {
        handler.handleOntologyChanges(List.of(removeAxiomChange));
        verify(removeAxiomChangeConsumer, times(1)).accept(any());
    }

    @Test
    public void shouldNotCallAddAxiomChangeConsumerOnRemoveAxiom() {
        handler.handleOntologyChanges(List.of(removeAxiomChange));
        verify(addAxiomChangeConsumer, never()).accept(any());
    }
}

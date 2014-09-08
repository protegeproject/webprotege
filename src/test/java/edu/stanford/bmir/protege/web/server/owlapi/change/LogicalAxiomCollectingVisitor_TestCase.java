package edu.stanford.bmir.protege.web.server.owlapi.change;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;

import java.util.Collection;

import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 07/09/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class LogicalAxiomCollectingVisitor_TestCase {

    @Mock
    private Collection<OWLLogicalAxiom> axiomCollection;

    @Mock
    private OWLLogicalAxiom axiom;

    private AddAxiomData addAxiomData;

    private RemoveAxiomData removeAxiomData;

    private LogicalAxiomCollectingVisitor visitor;

    @Before
    public void setUp() throws Exception {
        visitor = new LogicalAxiomCollectingVisitor(axiomCollection);
        addAxiomData = new AddAxiomData(axiom);
        removeAxiomData = new RemoveAxiomData(axiom);
    }

    @Test
    public void shouldAddAxiomToCollection() {
        when(axiom.isLogicalAxiom()).thenReturn(true);
        visitor.visit(addAxiomData);
        verify(axiomCollection, times(1)).add(axiom);
        verify(axiomCollection, never()).remove(axiom);
    }

    @Test
    public void shouldRemoveAxiomFromCollection() {
        when(axiom.isLogicalAxiom()).thenReturn(true);
        visitor.visit(removeAxiomData);
        verify(axiomCollection, times(1)).remove(axiom);
        verify(axiomCollection, never()).add(axiom);
    }

    @Test
    public void shouldNotAddAxiomToCollection() {
        when(axiom.isLogicalAxiom()).thenReturn(false);
        visitor.visit(addAxiomData);
        verify(axiomCollection, never()).add(axiom);
        verify(axiomCollection, never()).remove(axiom);
    }

    @Test
    public void shouldNotRemoveAxiomFromCollection() {
        when(axiom.isLogicalAxiom()).thenReturn(false);
        visitor.visit(removeAxiomData);
        verify(axiomCollection, never()).remove(axiom);
        verify(axiomCollection, never()).add(axiom);
    }
}

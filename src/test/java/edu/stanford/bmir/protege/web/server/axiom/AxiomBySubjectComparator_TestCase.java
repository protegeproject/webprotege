package edu.stanford.bmir.protege.web.server.axiom;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomBySubjectComparator;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Comparator;

import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomBySubjectComparator_TestCase {

    @Mock
    private Comparator<OWLObject> axiomSubjectComparator;

    @Mock
    private AxiomSubjectProvider axiomSubjectProvider;

    @Mock
    private OWLAxiom axiom1, axiom2;

    @Mock
    private OWLObject subject1, subject2;

    private AxiomBySubjectComparator comparator;

    @Before
    public void setUp() throws Exception {
        comparator = new AxiomBySubjectComparator(axiomSubjectProvider, axiomSubjectComparator);
        Mockito.doReturn(Optional.of(subject1)).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.of(subject2)).when(axiomSubjectProvider).getSubject(axiom2);
    }

    @Test
    public void shouldReturnSubjectComparatorValue() {
        when(axiomSubjectComparator.compare(subject1, subject2)).thenReturn(5);
        assertThat(comparator.compare(axiom1, axiom2), is(5));
    }


    @Test
    public void shouldReturnZero() {
        Mockito.doReturn(Optional.absent()).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.absent()).when(axiomSubjectProvider).getSubject(axiom2);
        assertThat(comparator.compare(axiom1, axiom2), is(0));
    }

    @Test
    public void shouldReturnMinusOne() {
        Mockito.doReturn(Optional.of(subject1)).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.absent()).when(axiomSubjectProvider).getSubject(axiom2);
        assertThat(comparator.compare(axiom1, axiom2), is(-1));
    }

    @Test
    public void shouldReturnPlusOne() {
        Mockito.doReturn(Optional.absent()).when(axiomSubjectProvider).getSubject(axiom1);
        Mockito.doReturn(Optional.of(subject2)).when(axiomSubjectProvider).getSubject(axiom2);
        assertThat(comparator.compare(axiom1, axiom2), is(1));
    }
}

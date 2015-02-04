package edu.stanford.bmir.protege.web.server.axiom;

import edu.stanford.bmir.protege.web.shared.axiom.AxiomByRenderingComparator;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomBySubjectComparator;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomByTypeComparator;
import edu.stanford.bmir.protege.web.shared.axiom.DefaultAxiomComparator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAxiom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAxiomComparator_TestCase {

    public static final int BY_SUBJECT_DIFF = -5;
    public static final int TYPE_DIFF = -7;
    public static final int RENDERING_DIFF = -10;
    public static final int COMPARE_SAME = 0;

    @Mock
    private AxiomBySubjectComparator bySubjectComparator;

    @Mock
    private AxiomByTypeComparator byTypeComparator;

    @Mock
    private AxiomByRenderingComparator byRenderingComparator;

    @Mock
    private OWLAxiom axiom1, axiom2;

    private DefaultAxiomComparator defaultAxiomComparator;

    @Before
    public void setUp() throws Exception {
        defaultAxiomComparator = new DefaultAxiomComparator(
                bySubjectComparator,
                byTypeComparator,
                byRenderingComparator);
    }

    @Test
    public void shouldCompareBySubjectFirst() {
        when(bySubjectComparator.compare(axiom1, axiom2)).thenReturn(BY_SUBJECT_DIFF);
        assertThat(defaultAxiomComparator.compare(axiom1, axiom2), is(BY_SUBJECT_DIFF));
        verify(byTypeComparator, never()).compare(axiom1, axiom2);
        verify(byRenderingComparator, never()).compare(axiom1, axiom2);
    }

    @Test
    public void shouldCompareByTypeSecond() {
        // Same subject.  Now compare by type
        when(bySubjectComparator.compare(axiom1, axiom2)).thenReturn(COMPARE_SAME);
        when(byTypeComparator.compare(axiom1, axiom2)).thenReturn(TYPE_DIFF);
        assertThat(defaultAxiomComparator.compare(axiom1, axiom2), is(TYPE_DIFF));
        verify(byRenderingComparator, never()).compare(axiom1, axiom2);
    }

    @Test
    public void shouldCompareByRenderingThird() {
        // Same subject.  Same type.  Not compare by rendering.
        when(bySubjectComparator.compare(axiom1, axiom2)).thenReturn(COMPARE_SAME);
        when(byTypeComparator.compare(axiom1, axiom2)).thenReturn(COMPARE_SAME);
        when(byRenderingComparator.compare(axiom1, axiom2)).thenReturn(RENDERING_DIFF);
        assertThat(defaultAxiomComparator.compare(axiom1, axiom2), is(RENDERING_DIFF));
    }
}

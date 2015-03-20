package edu.stanford.bmir.protege.web.server.axiom;

import edu.stanford.bmir.protege.web.shared.axiom.AxiomByRenderingComparator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomByRenderingComparator_TestCase {

    private AxiomByRenderingComparator renderingComparator;

    @Mock
    private OWLObjectRenderer renderer;

    @Mock
    private OWLAxiom axiom1, axiom2;

    @Before
    public void setUp() throws Exception {
        renderingComparator = new AxiomByRenderingComparator(renderer);
    }

    @Test
    public void shouldCompareByRendering() {
        when(renderer.render(axiom1)).thenReturn("A");
        when(renderer.render(axiom2)).thenReturn("B");
        assertThat(renderingComparator.compare(axiom1, axiom2), is(lessThan(0)));
    }

    @Test
    public void shouldIgnoreCase() {
        when(renderer.render(axiom1)).thenReturn("a");
        when(renderer.render(axiom2)).thenReturn("A");
        assertThat(renderingComparator.compare(axiom1, axiom2), is(0));
    }
}

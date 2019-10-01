package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ConflictingBooleanValuesMatcher_TestCase {

    private ConflictingBooleanValuesMatcher matcher;

    @Mock
    private AnnotationAssertionAxiomsIndex hasAxioms;

    private Set<OWLAnnotationAssertionAxiom> axioms = new HashSet<>();

    @Mock
    private OWLAnnotationAssertionAxiom axiomA, axiomB;

    @Mock
    private OWLAnnotationProperty prop, propA, propB;

    @Mock
    private OWLLiteral literalTrue, literalFalse;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI iri;

    @Before
    public void setUp() {
        axioms.clear();
        matcher = new ConflictingBooleanValuesMatcher(hasAxioms);
        when(axiomA.getProperty()).thenReturn(propA);
        when(axiomB.getProperty()).thenReturn(propB);


        when(entity.getIRI()).thenReturn(iri);

        when(hasAxioms.getAnnotationAssertionAxioms(any())).thenReturn(axioms.stream());
        axioms.add(axiomA);
        axioms.add(axiomB);

        when(literalTrue.isBoolean()).thenReturn(true);
        when(literalTrue.getLiteral()).thenReturn("true");

        when(literalFalse.isBoolean()).thenReturn(true);
        when(literalFalse.getLiteral()).thenReturn("false");
    }

    @Test
    public void shouldNotMatchDifferentPropertyDifferentValues() {
        when(axiomA.getValue()).thenReturn(literalTrue);
        when(axiomB.getValue()).thenReturn(literalFalse);

        assertThat(matcher.matches(entity), is(false));
    }

    @Test
    public void shouldNotMatchDifferentPropertySameValues() {
        when(axiomA.getValue()).thenReturn(literalTrue);
        when(axiomB.getValue()).thenReturn(literalTrue);

        assertThat(matcher.matches(entity), is(false));
    }

    @Test
    public void shouldMatchSamePropertyDifferentValues() {
        when(axiomA.getProperty()).thenReturn(propA);
        when(axiomB.getProperty()).thenReturn(propA);
        when(axiomA.getValue()).thenReturn(literalTrue);
        when(axiomB.getValue()).thenReturn(literalFalse);

        assertThat(matcher.matches(entity), is(true));
    }
}

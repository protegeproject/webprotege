package edu.stanford.bmir.protege.web.client.form;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class ParentClassAxiomParser_TestCase {

    private ParentClassAxiomParser parser;

    @Before
    public void setUp() {
        parser = new ParentClassAxiomParser();
    }

    @Test
    public void shouldParseClassAssertion() {
        String ax = "ClassAssertion(<http://example.org/A> ${subject.iri})";
        Optional<OWLClass> cls = parser.parseParentAxiom(ax);
        assertThat(cls.isPresent(), is(true));
        assertThat(cls.get().getIRI().toString(), is("http://example.org/A"));
    }

    @Test
    public void shouldParseSubClassOf() {
        String ax = "SubClassOf(${subject.iri} <http://example.org/A>)";
        Optional<OWLClass> cls = parser.parseParentAxiom(ax);
        assertThat(cls.isPresent(), is(true));
        assertThat(cls.get().getIRI().toString(), is("http://example.org/A"));
    }
}

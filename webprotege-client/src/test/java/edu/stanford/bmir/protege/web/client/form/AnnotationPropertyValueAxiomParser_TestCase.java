package edu.stanford.bmir.protege.web.client.form;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-22
 */
public class AnnotationPropertyValueAxiomParser_TestCase {

    private AnnotationPropertyValueAxiomParser parser;

    @Before
    public void setUp() {
        parser = new AnnotationPropertyValueAxiomParser();
    }

    @Test
    public void shouldParseAnnotationWithLiteral() {
        Optional<OWLAnnotation> annotation = parser.parseAnnotation("AnnotationAssertion(<http://example.org/prop> ${subject.iri} \"Hello World\")");
        assertThat(annotation.isPresent(), is(true));
        assertThat(annotation.get().getProperty().getIRI().toString(), is("http://example.org/prop"));
        assertThat(((OWLLiteral) annotation.get().getValue()).getLiteral(), is("Hello World"));
        assertThat(((OWLLiteral) annotation.get().getValue()).getLang(), is(""));
    }

    @Test
    public void shouldParseAnnotationWithIri() {
        Optional<OWLAnnotation> annotation = parser.parseAnnotation("AnnotationAssertion(<http://example.org/prop> ${subject.iri} <http://example.org/A>)");
        assertThat(annotation.isPresent(), is(true));
        assertThat(annotation.get().getProperty().getIRI().toString(), is("http://example.org/prop"));
        assertThat(((IRI) annotation.get().getValue()).toString(), is("http://example.org/A"));
    }
}

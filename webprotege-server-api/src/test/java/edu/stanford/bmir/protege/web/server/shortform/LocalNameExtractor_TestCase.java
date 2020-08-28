package edu.stanford.bmir.protege.web.server.shortform;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Mar 2018
 */
public class LocalNameExtractor_TestCase {

    private LocalNameExtractor extractor;

    @Before
    public void setUp() {
        extractor = new LocalNameExtractor();
        URI uri;
    }

    @Test
    public void shouldExtractLocalNameFromLastPathElement() {
        String localName = extractor.getLocalName(IRI.create("scheme://the.host.address:8080/path/elements/thename"));
        assertThat(localName, is("thename"));
    }


    @Test
    public void shouldExtractLocalNameFromLastPathElementWithQuery() {
        String localName = extractor.getLocalName(IRI.create("scheme://the.host.address:8080/path/elements/thename?x=y;a=b"));
        assertThat(localName, is("thename?x=y;a=b"));
    }


    @Test
    public void shouldNotExtractLocalNameFromLastPathElement() {
        String localName = extractor.getLocalName(IRI.create("scheme://the.host.address:8080/path/elements/final/"));
        assertThat(localName, is(""));
    }

    @Test
    public void shouldExtractLocalNameFromFragment() {
        String localName = extractor.getLocalName(IRI.create("scheme://the.host.address:8080/path/elements/final#thename"));
        assertThat(localName, is("thename"));
    }

    @Test
    public void shouldExtractDecodedString() {
        assertThat(extractor.getLocalName(IRI.create("http://other.com/path#gro%C3%9F")), is("groß"));
    }

    @Test
    public void shouldExtractDecodedUtf16() {
        assertThat(extractor.getLocalName(IRI.create("http://other.com/path#%E9%96%80%E7%94%9F")), is("門生"));
    }

    @Test
    public void shouldExtractDecodedFragment() {
        assertThat(extractor.getLocalName(IRI.create("http://other.com/path#With%23Hash")), is("With#Hash"));
    }

}

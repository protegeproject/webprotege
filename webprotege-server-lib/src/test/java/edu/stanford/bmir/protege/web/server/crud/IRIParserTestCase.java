package edu.stanford.bmir.protege.web.server.crud;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 17/04/2014
 */
public class IRIParserTestCase {

    private IRIParser parser;

    @Before
    public void setUp() {
        parser = new IRIParser();
    }

    @Test
    public void shouldReturnAbsentForEmptyString() {
        Optional<IRI> value = parser.parseIRI("");
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnAbsentForEmptyIRIContent() {
        Optional<IRI> value = parser.parseIRI("<>");
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnAbsentForContentWithSpace() {
        Optional<IRI> value = parser.parseIRI("<a b>");
        assertThat(value.isPresent(), is(false));
    }

    @Test
    public void shouldReturnIRIWithParseableContent() {
        String content = "stuff.com";
        Optional<IRI> value = parser.parseIRI("<" + content + ">");
        assertThat(value.isPresent(), is(true));
        assertThat(value.get().toString(), is(equalTo(content)));
    }


}

package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class IriLexicalValueMatcher_TestCase {

    public IriLexicalValueMatcher matcher;

    @Mock
    private Matcher<String> lexicalValueMatcher;

    @Mock
    private IRI iri;

    private String lexicalValue = "The Lexical Value";

    @Before
    public void setUp() {
        matcher = new IriLexicalValueMatcher(lexicalValueMatcher);

        when(iri.toString()).thenReturn(lexicalValue);
    }

    @Test
    public void shouldMatchLexicalValue() {
        when(lexicalValueMatcher.matches(lexicalValue)).thenReturn(true);
        assertThat(matcher.matches(iri), is(true));
    }
}

package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class LiteralMatcher_TestCase {

    private static final String THE_LEXICAL_VALUE = "The Lexical Value";

    private static final String THE_LANG = "THE_LANG";

    private LiteralMatcher matcher;

    @Mock
    private Matcher<String> lexicalValueMatcher;

    @Mock
    private Matcher<String> langMatcher;

    @Mock
    private Matcher<OWLDatatype> datatypeMatcher;

    @Mock
    private OWLDatatype theDatatype;

    @Mock
    private OWLLiteral literal;


    @Before
    public void setUp() throws Exception {
        matcher = new LiteralMatcher(lexicalValueMatcher,
                                     langMatcher,
                                     datatypeMatcher);
        when(literal.getLiteral()).thenReturn(THE_LEXICAL_VALUE);
        when(literal.getLang()).thenReturn(THE_LANG);
        when(literal.getDatatype()).thenReturn(theDatatype);

        when(lexicalValueMatcher.matches(THE_LEXICAL_VALUE)).thenReturn(true);
        when(langMatcher.matches(THE_LANG)).thenReturn(true);
        when(datatypeMatcher.matches(theDatatype)).thenReturn(true);
    }

    @Test
    public void shouldMatchAll() {
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldMatchLexicalValue() {
        when(lexicalValueMatcher.matches(THE_LEXICAL_VALUE)).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldMatchLang() {
        when(langMatcher.matches(THE_LANG)).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldMatchDatatype() {
        when(datatypeMatcher.matches(theDatatype)).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }
}

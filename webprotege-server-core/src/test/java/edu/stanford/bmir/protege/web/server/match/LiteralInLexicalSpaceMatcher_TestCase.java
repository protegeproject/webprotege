package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class LiteralInLexicalSpaceMatcher_TestCase {

    private LiteralInLexicalSpaceMatcher matcher;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLDatatype datatype;

    @Before
    public void setUp() {
        matcher = new LiteralInLexicalSpaceMatcher();
        when(literal.getDatatype()).thenReturn(datatype);
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_INTEGER);
    }

    @Test
    public void shouldMatchLiteralInLexicalSpace() {
        when(literal.getLiteral()).thenReturn("33");
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotMatchLiteralNotInLexicalSpace() {
        when(literal.getLiteral()).thenReturn("abc");
        assertThat(matcher.matches(literal), is(false));
    }
}

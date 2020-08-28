package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class IndexingAnalyzerWrapper_TestCase {

    public static final int MIN_GRAM_SIZE = 2;

    public static final int MAX_GRAM_SIZE = 5;

    private IndexingAnalyzerWrapper analyzer;

    public static final String FIELD_NAME = "theField";

    @Before
    public void setUp() throws Exception {
        analyzer = new IndexingAnalyzerWrapper(MIN_GRAM_SIZE, MAX_GRAM_SIZE);
    }


    @Test
    public void shouldSplitPrefixedNames() throws IOException {
        assertHasTokens("rdfs:label", "rd", "rdf", "rdfs", "la", "lab", "labe", "label", "rdfslabel");
    }

    @Test
    public void shouldTokenizeNumbers() throws IOException {
        assertHasTokens("320", "32", "320");

    }

    @Test
    public void shouldTokenizeCamelCase() throws IOException {
        assertHasTokens("hasPart", "ha", "has", "pa", "par", "part", "haspart");
    }

    @Test
    public void shouldTokenizeNumbersCamelCase() throws IOException {
        assertHasTokens("A320", "a", "32", "320");
    }

    @Test
    public void shouldTokenizeAroundHyphens() throws IOException {
        assertHasTokens("Non-Polar", "no", "non", "po", "pol", "pola", "polar", "nonpolar");
    }

    @Test
    public void shouldTokenizeAroundUnderscors() throws IOException {
        assertHasTokens("Non_Polar", "no", "non", "po", "pol", "pola", "polar", "nonpolar");
    }

    @Test
    public void shouldAddLongTokens() throws IOException {
        String longFieldValue = "thisisalongtokenwithmorethantwentycharacters";
        assertHasTokens(longFieldValue, "th", "thi", "this", "thisi", longFieldValue);
    }

    private void assertHasTokens(String fieldValue, String... tokens) throws IOException {

        var wrappedAnalyzer = analyzer.getWrappedAnalyzer(FIELD_NAME);
        var tokenStream = wrappedAnalyzer.tokenStream(FIELD_NAME, fieldValue);
        var textAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        var tokenList = new ArrayList<String>();
        while (tokenStream.incrementToken()) {
            String e = textAttribute.toString();
            tokenList.add(e);
        }
        assertThat(tokenList, hasItems(tokens));
    }
}
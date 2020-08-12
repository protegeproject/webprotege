package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class QueryAnalyzerFactory_TestCase {

    public static final String FIELD_NAME = "fieldName";
    Analyzer analyzer;

    @Before
    public void setUp() throws Exception {
        analyzer = new QueryAnalyzerFactory().getTokenizedQueryAnalyzer();
    }

    @Test
    public void shouldSearchPrefixNames() throws IOException {
        assertHasTokens("rdfs:label",
                        "rdfs",
                        "label",
                        "rdfslabel");
    }

    @Test
    public void shouldSearchCamelCase() throws IOException {
        assertHasTokens("A320",
                       "a",
                        "320");
    }

    @Test
    public void shouldSearchDiacritics() throws IOException {
        assertHasTokens("Protégé",
                        "protege");
    }

    @Test
    public void shouldSearchHyphens() throws IOException {
        assertHasTokens("Non-Polar",
                        "non",
                        "polar",
                        "nonpolar");
    }

    @Test
    public void shouldSearchUnderscores() throws IOException {
        assertHasTokens("Non_Polar",
                        "non",
                        "polar",
                        "nonpolar");
    }

    private void assertHasTokens(String fieldValue,
                                 String ... tokens) throws IOException {

        var tokenStream = analyzer.tokenStream(FIELD_NAME, fieldValue);
        var textAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        var tokenList = new ArrayList<String>();
        while(tokenStream.incrementToken()) {
            String e = textAttribute.toString();
            tokenList.add(e);
        }
        assertThat(tokenList, containsInAnyOrder(tokens));
    }
}
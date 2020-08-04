package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Attribute;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class IndexingAnalyzerWrapper_TestCase {

    private IndexingAnalyzerWrapper analyzer;

    public static final String FIELD_NAME = "theField";

    @Before
    public void setUp() throws Exception {
        analyzer = new IndexingAnalyzerWrapper();
    }


    @Test
    public void shouldSplitPrefixedNames() throws IOException {
        assertHasTokens("rdfs:label",
                        "rd", "rdf", "rdfs", "la", "lab", "labe", "label", "rdfslabel");
    }

    @Test
    public void shouldTokenizeNumbers() throws IOException {
        assertHasTokens("320",
                        "32", "320");

    }

    @Test
    public void shouldTokenizeCamelCase() throws IOException {
        assertHasTokens("hasPart",
                        "ha", "has", "pa", "par", "part", "haspart");
    }

    @Test
    public void shouldTokenizeNumbersCamelCase() throws IOException {
        assertHasTokens("A320",
                        "a", "32", "320");
    }

    @Test
    public void shouldTokenizeAroundHyphens() throws IOException {
        assertHasTokens("Non-Polar",
                        "no",
                        "non",
                        "po",
                        "pol",
                        "pola",
                        "polar",
                        "nonpolar");
    }

    @Test
    public void shouldTokenizeAroundUnderscors() throws IOException {
        assertHasTokens("Non_Polar",
                        "no",
                        "non",
                        "po",
                        "pol",
                        "pola",
                        "polar",
                        "nonpolar");
    }

    @Test
    public void shouldAddLongTokens() throws IOException {
        String longFieldValue = "thisisalongtokenwithmorethantwentycharacters";
        assertHasTokens(longFieldValue,
                        "th", longFieldValue);
    }

    private void assertHasTokens(String fieldValue,
                                 String ... tokens) throws IOException {

        var wrappedAnalyzer = analyzer.getWrappedAnalyzer(FIELD_NAME);
        var tokenStream = wrappedAnalyzer.tokenStream(FIELD_NAME, fieldValue);
        var textAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        var tokenList = new ArrayList<String>();
        while(tokenStream.incrementToken()) {
            String e = textAttribute.toString();
            tokenList.add(e);
        }
        assertThat(tokenList, hasItems(tokens));
    }
}
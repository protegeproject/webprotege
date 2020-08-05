package edu.stanford.bmir.protege.web.shared.shortform;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;

public class DictionaryLanguage_Serialization_TestCase {

    @Test
    public void shouldSerializeLocalNameDictionaryLanguage() throws IOException {
        var dictionaryLanguage = DictionaryLanguage.localName();
        JsonSerializationTestUtil.testSerialization(dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldSerializeAnnotationBasedDictionaryLanguage() throws IOException {
        var dictionaryLanguage = DictionaryLanguage.rdfsLabel("en");
        JsonSerializationTestUtil.testSerialization(dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldDeserializeLocalNameLegacySerialization() throws IOException {
        var localName = DictionaryLanguage.localName();
        JsonSerializationTestUtil.testDeserialization("{}", localName, DictionaryLanguage.class);
    }

    @Test
    public void shouldDeserializeAnnotationAssertionWithEmptyLanguageTagLegacySerialization() throws IOException {
        var iriString = "http://example.org/prop";
        var dictionaryLanguage = DictionaryLanguage.create(IRI.create(iriString), "");
        var serialization = String.format("{\"propertyIri\":\"%s\"}", iriString);
        JsonSerializationTestUtil.testDeserialization(serialization, dictionaryLanguage, DictionaryLanguage.class);
    }

    @Test
    public void shouldDeserializeAnnotationAssertionWithNonEmptyLanguageTagLegacySerialization() throws IOException {
        var iriString = "http://example.org/prop";
        var dictionaryLanguage = DictionaryLanguage.create(IRI.create(iriString), "en");
        var serialization = String.format("{\"propertyIri\":\"%s\", \"lang\":\"en\"}", iriString);
        JsonSerializationTestUtil.testDeserialization(serialization, dictionaryLanguage, DictionaryLanguage.class);
    }
}

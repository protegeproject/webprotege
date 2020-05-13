package edu.stanford.bmir.protege.web.shared.shortform;

import edu.stanford.bmir.protege.web.server.shortform.Dictionary;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

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
}

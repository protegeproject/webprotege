package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Aug 2018
 */
public class DictionaryLanguageIO_TestCase {

    private static final DictionaryLanguageData FIRST_LANGUAGE = DictionaryLanguageData.rdfsLabel("en");

    private static final DictionaryLanguageData SECOND_LANGUAGE = DictionaryLanguageData.rdfsLabel("fr");

    private static final DictionaryLanguageData EMPTY_LANG = DictionaryLanguageData.rdfsLabel("");

    private static final DictionaryLanguageData LOCAL_NAME_LANGUAGE = DictionaryLanguageData.localName();



    @Test
    public void shouldSerializeToString() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        String out = io.write(ImmutableList.of(FIRST_LANGUAGE,
                                               SECOND_LANGUAGE,
                                               EMPTY_LANG,
                                               LOCAL_NAME_LANGUAGE));
        assertThat(out, is("http://www.w3.org/2000/01/rdf-schema#label@en|http://www.w3.org/2000/01/rdf-schema#label@fr|http://www.w3.org/2000/01/rdf-schema#label@|@"));
    }

    @Test
    public void shouldParseFromString() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguageData> dictionaryLanguages = io.parse("http://www.w3.org/2000/01/rdf-schema#label@en|http://www.w3.org/2000/01/rdf-schema#label@fr");
        assertThat(dictionaryLanguages, contains(FIRST_LANGUAGE, SECOND_LANGUAGE));
    }

    @Test
    public void shouldParseEmptyLang() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguageData> dictionaryLanguages = io.parse("http://www.w3.org/2000/01/rdf-schema#label@");
        assertThat(dictionaryLanguages, contains(EMPTY_LANG));
    }

    @Test
    public void shouldParseEmptyIriAndLang() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguageData> dictionaryLanguages = io.parse("@");
        assertThat(dictionaryLanguages, contains(LOCAL_NAME_LANGUAGE));
    }

    @Test
    public void shouldParseEmptyString() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguageData> dictionaryLanguages = io.parse("");
        assertThat(dictionaryLanguages, is(ImmutableList.of()));
    }
}

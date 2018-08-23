package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Aug 2018
 */
public class DictionaryLanguageIO_TestCase {

    private static final DictionaryLanguage FIRST_LANGUAGE = DictionaryLanguage.rdfsLabel("en");

    private static final DictionaryLanguage SECOND_LANGUAGE = DictionaryLanguage.rdfsLabel("fr");

    private static final DictionaryLanguage EMPTY_LANG = DictionaryLanguage.rdfsLabel("");

    private static final DictionaryLanguage LOCAL_NAME_LANGUAGE = DictionaryLanguage.localName();



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
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("http://www.w3.org/2000/01/rdf-schema#label@en|http://www.w3.org/2000/01/rdf-schema#label@fr");
        assertThat(dictionaryLanguages, contains(FIRST_LANGUAGE, SECOND_LANGUAGE));
    }

    @Test
    public void shouldParseEmptyLang() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("http://www.w3.org/2000/01/rdf-schema#label@");
        assertThat(dictionaryLanguages, contains(EMPTY_LANG));
    }

    @Test
    public void shouldParseEmptyIriAndLang() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("@");
        assertThat(dictionaryLanguages, contains(LOCAL_NAME_LANGUAGE));
    }

    @Test
    public void shouldParseEmptyString() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("");
        assertThat(dictionaryLanguages, is(ImmutableList.of()));
    }
}

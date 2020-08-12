package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.junit.Before;
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
    private DictionaryLanguageIO io;


    @Before
    public void setUp() throws Exception {
        io = new DictionaryLanguageIO();
    }

    @Test
    public void shouldSerializeToString() {
        String out = io.write(ImmutableList.of(FIRST_LANGUAGE,
                                               SECOND_LANGUAGE,
                                               EMPTY_LANG,
                                               LOCAL_NAME_LANGUAGE));
        assertThat(out, is("[AnnotationAssertion]en@http://www.w3.org/2000/01/rdf-schema#label!![AnnotationAssertion]fr@http://www.w3.org/2000/01/rdf-schema#label!![AnnotationAssertion]@http://www.w3.org/2000/01/rdf-schema#label!![LocalName]"));
    }

    @Test
    public void shouldParseFromString() {
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("[AnnotationAssertion]en@http://www.w3.org/2000/01/rdf-schema#label!![AnnotationAssertion]fr@http://www.w3.org/2000/01/rdf-schema#label");
        assertThat(dictionaryLanguages, contains(FIRST_LANGUAGE, SECOND_LANGUAGE));
    }

    @Test
    public void shouldParseEmptyLang() {
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("[AnnotationAssertion]@http://www.w3.org/2000/01/rdf-schema#label");
        assertThat(dictionaryLanguages, contains(EMPTY_LANG));
    }

    @Test
    public void shouldParseEmptyIriAndLang() {
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("[AnnotationAssertion]@");
        assertThat(dictionaryLanguages, contains(LOCAL_NAME_LANGUAGE));
    }

    @Test
    public void shouldParseLocalName() {
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("[LocalName]");
        assertThat(dictionaryLanguages, contains(LOCAL_NAME_LANGUAGE));
    }

    @Test
    public void shouldParseOboId() {
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("[OboId]");
        assertThat(dictionaryLanguages, contains(DictionaryLanguage.oboId()));
    }

    @Test
    public void shouldParsePrefixedname() {
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("[PrefixedName]");
        assertThat(dictionaryLanguages, contains(DictionaryLanguage.prefixedName()));
    }


    @Test
    public void shouldParseEmptyString() {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        ImmutableList<DictionaryLanguage> dictionaryLanguages = io.parse("");
        assertThat(dictionaryLanguages, is(ImmutableList.of()));
    }
}

package edu.stanford.bmir.protege.web.shared.shortform;

import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageFilter.EmptyLangTagTreatment.EXCLUDE_EMPTY_LANG_TAGS;
import static edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageFilter.EmptyLangTagTreatment.INCLUDE_EMPTY_LANG_TAGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryLanguageFilter_TestCase {

    public static final String LANG_TAG = "en";

    public static final String EMPTY_LANG_TAG = "";

    @Mock
    private LangTagFilter langTagFilter;

    @Mock
    private DictionaryLanguage dictionaryLanguage;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldIncludeEmptyLangTag() {
        when(dictionaryLanguage.getLang())
                .thenReturn(EMPTY_LANG_TAG);
        DictionaryLanguageFilter filter = DictionaryLanguageFilter.get(langTagFilter, INCLUDE_EMPTY_LANG_TAGS);
        assertThat(filter.isIncluded(dictionaryLanguage), is(true));
    }

    @Test
    public void shouldNotIncludeEmptyLangTag() {
        when(dictionaryLanguage.getLang())
                .thenReturn(EMPTY_LANG_TAG);
        DictionaryLanguageFilter filter = DictionaryLanguageFilter.get(langTagFilter, EXCLUDE_EMPTY_LANG_TAGS);
        assertThat(filter.isIncluded(dictionaryLanguage), is(false));
    }

    @Test
    public void shouldIncludeNonEmptyLangTag() {
        when(dictionaryLanguage.getLang())
                .thenReturn(LANG_TAG);
        when(langTagFilter.isIncluded(LANG_TAG))
                .thenReturn(true);
        DictionaryLanguageFilter filter = DictionaryLanguageFilter.get(langTagFilter, EXCLUDE_EMPTY_LANG_TAGS);
        assertThat(filter.isIncluded(dictionaryLanguage), is(true));
    }

    @Test
    public void shouldNotIncludeNonEmptyLangTag() {
        when(dictionaryLanguage.getLang())
                .thenReturn(LANG_TAG);
        when(langTagFilter.isIncluded(LANG_TAG))
                .thenReturn(false);
        DictionaryLanguageFilter filter = DictionaryLanguageFilter.get(langTagFilter, EXCLUDE_EMPTY_LANG_TAGS);
        assertThat(filter.isIncluded(dictionaryLanguage), is(false));
    }
}
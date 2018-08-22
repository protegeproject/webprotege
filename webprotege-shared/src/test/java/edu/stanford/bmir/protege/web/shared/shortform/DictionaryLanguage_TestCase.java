
package edu.stanford.bmir.protege.web.shared.shortform;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryLanguage_TestCase {

    @Mock
    private IRI annotationIri;

    private String lang = "the-lang";

    private DictionaryLanguage dictionaryLanguage;

    @Before
    public void setUp() {
        dictionaryLanguage = DictionaryLanguage.create(annotationIri, lang);
    }

    @Test
    public void shouldAcceptNullPropertyIri() {
        DictionaryLanguage dictionaryLanguage = DictionaryLanguage.create(null, "");
        assertThat(dictionaryLanguage.getAnnotationPropertyIri(), is(nullValue()));
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAcceptNullLang() {
        DictionaryLanguage dictionaryLanguage = DictionaryLanguage.create(annotationIri, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(dictionaryLanguage, is(dictionaryLanguage));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(dictionaryLanguage.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(dictionaryLanguage, is(DictionaryLanguage.create(annotationIri, lang)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(dictionaryLanguage.hashCode(), is(DictionaryLanguage.create(annotationIri, lang).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(dictionaryLanguage.toString(), Matchers.startsWith("DictionaryLanguage"));
    }

    @Test
    public void shouldReturn_true_For_isLanguageFor() {
        MatcherAssert.assertThat(dictionaryLanguage.matches(annotationIri, lang), is(true));
    }

    @Test
    public void shouldReturn_false_For_isLanguageFor() {
        MatcherAssert.assertThat(dictionaryLanguage.matches(annotationIri, "other-lang"), is(false));
        MatcherAssert.assertThat(dictionaryLanguage.matches(Mockito.mock(IRI.class), lang), is(false));
    }

    @Test
    public void shouldReturnSuppliedIri() {
        MatcherAssert.assertThat(dictionaryLanguage.getAnnotationPropertyIri(), is(annotationIri));
    }

    @Test
    public void shouldReturnSuppliedLang() {
        MatcherAssert.assertThat(dictionaryLanguage.getLang(), is(lang));
    }

}

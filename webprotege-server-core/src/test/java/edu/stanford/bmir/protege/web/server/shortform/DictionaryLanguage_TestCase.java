
package edu.stanford.bmir.protege.web.server.shortform;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;

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
    public void shouldBeEqualToSelf() {
        assertThat(dictionaryLanguage, is(dictionaryLanguage));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(dictionaryLanguage.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(dictionaryLanguage, is(DictionaryLanguage.create(annotationIri, lang)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(dictionaryLanguage.hashCode(), is(DictionaryLanguage.create(annotationIri, lang).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(dictionaryLanguage.toString(), Matchers.startsWith("DictionaryLanguage"));
    }

    @Test
    public void shouldReturn_true_For_isLanguageFor() {
        assertThat(dictionaryLanguage.matches(annotationIri, lang), is(true));
    }

    @Test
    public void shouldReturn_false_For_isLanguageFor() {
        assertThat(dictionaryLanguage.matches(annotationIri, "other-lang"), is(false));
        assertThat(dictionaryLanguage.matches(mock(IRI.class), lang), is(false));
    }

    @Test
    public void shouldReturnSuppliedIri() {
        assertThat(dictionaryLanguage.getAnnotationPropertyIri(), is(annotationIri));
    }

    @Test
    public void shouldReturnSuppliedLang() {
        assertThat(dictionaryLanguage.getLang(), is(lang));
    }

}

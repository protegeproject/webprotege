package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Sep 2018
 */
public class DefaultDisplayNameSettingsFactory_TestCase {

    private DefaultDisplayNameSettingsFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultDisplayNameSettingsFactory();
    }

    @Test
    public void shouldCreateDefaultSettingsForEmptyLang() {
        DisplayNameSettings settings = factory.getDefaultDisplayNameSettings("");
        assertThat(settings.getPrimaryDisplayNameLanguages(), is(ImmutableList.of(DictionaryLanguage.rdfsLabel(""),
                                                                                  DictionaryLanguage.prefixedName(),
                                                                                  DictionaryLanguage.localName())));
    }

    @Test
    public void shouldCreateDefaultSettingsForNonEmptyLang() {
        DisplayNameSettings settings = factory.getDefaultDisplayNameSettings("en-GB");
        assertThat(settings.getPrimaryDisplayNameLanguages(), is(ImmutableList.of(DictionaryLanguage.rdfsLabel("en-GB"),
                                                                                  DictionaryLanguage.rdfsLabel(""),
                                                                                  DictionaryLanguage.prefixedName(),
                                                                                  DictionaryLanguage.localName())));
    }
}

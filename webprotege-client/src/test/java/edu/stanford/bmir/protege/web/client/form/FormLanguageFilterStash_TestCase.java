package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.portlet.HasNodeProperties;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
@RunWith(MockitoJUnitRunner.class)
public class FormLanguageFilterStash_TestCase {

    private FormLanguageFilterStash stash;

    private ImmutableList<LangTag> langTags = ImmutableList.of(LangTag.get("en"),
                                                               LangTag.get("id"));

    @Mock
    private HasNodeProperties nodeProperties;

    @Before
    public void setUp() {
        stash = new FormLanguageFilterStash(nodeProperties);
    }

    @Test
    public void shouldStashLanguage() {
        stash.stashLanguage(langTags);
        verify(nodeProperties, times(1))
                .setNodeProperty(FormLanguageFilterStash.FORMS_LANGUAGE_FILTER,
                                 "en,id");

    }

    @Test
    public void shouldGetStashedLangTags() {
        when(nodeProperties.getNodeProperty(FormLanguageFilterStash.FORMS_LANGUAGE_FILTER, ""))
                .thenReturn("en,id");
        ImmutableList<LangTag> stashedlangTags = stash.getStashedLangTags();
        assertThat(stashedlangTags, is(equalTo(langTags)));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfLangTagsIsNull() {
        stash.stashLanguage(null);
    }
}

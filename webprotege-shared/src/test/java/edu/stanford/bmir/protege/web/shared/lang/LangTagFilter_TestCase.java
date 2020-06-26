package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@RunWith(MockitoJUnitRunner.class)
public class LangTagFilter_TestCase {

    @Mock
    private LangTag langTag, otherLangTag;

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullSet() {
        LangTagFilter.get(null);
    }

    @Test
    public void shouldIncludeAnyLangTagForEmptySet() {
        LangTagFilter langTagFilter = LangTagFilter.get(ImmutableSet.of());
        assertThat(langTagFilter.isIncluded(langTag), is(true));
    }

    @Test
    public void shouldIncludeAnyExplicitLangTag() {
        LangTagFilter langTagFilter = LangTagFilter.get(ImmutableSet.of(langTag));
        assertThat(langTagFilter.isIncluded(langTag), is(true));
    }


    @Test
    public void shouldNotIncludeAnyNonExplicityLangTagInNonEmptySet() {
        LangTagFilter langTagFilter = LangTagFilter.get(ImmutableSet.of(langTag));
        assertThat(langTagFilter.isIncluded(otherLangTag), is(false));
    }
}


package edu.stanford.bmir.protege.web.shared.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(MockitoJUnitRunner.class)
public class PrefixDeclaration_TestCase {

    private static final String PREFIX_NAME = "p:";

    private static final String PREFIX = "http://prefix.com/prefix/";

    private PrefixDeclaration prefixDeclaration;

    @Before
    public void setUp() {
        prefixDeclaration = PrefixDeclaration.get(PREFIX_NAME, PREFIX);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(prefixDeclaration, is(prefixDeclaration));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(prefixDeclaration.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(prefixDeclaration, is(PrefixDeclaration.get(PREFIX_NAME, PREFIX)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(prefixDeclaration.hashCode(), is(PrefixDeclaration.get(PREFIX_NAME, PREFIX).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(prefixDeclaration.toString(), startsWith("PrefixDeclaration"));
    }

    @Test
    public void should_getPrefixName() {
        assertThat(prefixDeclaration.getPrefixName(), is(PREFIX_NAME));
    }

    @Test
    public void should_getPrefix() {
        assertThat(prefixDeclaration.getPrefix(), is(PREFIX));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotPremitPrefixNameThatDoesNotEndWithColon() {
        PrefixDeclaration decl = PrefixDeclaration.get("a", "http://something.com/");
    }

    @Test
    public void shouldReportSlashAsACommonTerminatingCharacter() {
        PrefixDeclaration decl = PrefixDeclaration.get("a:", "http://something.com/ont/");
        assertThat(decl.isPrefixWithCommonTerminatingCharacter(), is(true));
    }

    @Test
    public void shouldReportHashAsACommonTerminatingCharacter() {
        PrefixDeclaration decl = PrefixDeclaration.get("a:", "http://something.com/ont#");
        assertThat(decl.isPrefixWithCommonTerminatingCharacter(), is(true));
    }

    @Test
    public void shouldReportUnderscoreAsACommonTerminatingCharacter() {
        PrefixDeclaration decl = PrefixDeclaration.get("a:", "http://something.com/ont_");
        assertThat(decl.isPrefixWithCommonTerminatingCharacter(), is(true));
    }
}

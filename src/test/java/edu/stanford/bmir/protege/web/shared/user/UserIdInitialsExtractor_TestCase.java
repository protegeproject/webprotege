package edu.stanford.bmir.protege.web.shared.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class UserIdInitialsExtractor_TestCase {

    @Mock
    private UserId userId;

    @Test
    public void shouldExtractFirstAndLastNameInitials() {
        extractInitials("First Last", "FL");
    }

    @Test
    public void shouldExtractFirstAndLastNameInitialsIgnoringMiddleName() {
        extractInitials("First Middle Last", "FL");
    }

    @Test
    public void shouldExtractFirstNameInitial() {
        extractInitials("First", "F");
    }

    @Test
    public void shouldExtractInitialsAroundDots() {
        extractInitials("First.Last", "FL");
    }

    @Test
    public void shouldNotIncludeQuotationMarks() {
        extractInitials("\"First Last\"", "FL");
    }

    @Test
    public void shouldExtractIntialsFromEmailAddress() {
        extractInitials("First.Last@email.com", "FL");
    }

    @Test
    public void shouldIgnoreCase() {
        extractInitials("first last", "FL");
    }

    @Test
    public void shouldExtractTwitterName() {
        extractInitials("@First", "F");
    }

    @Test
    public void shouldExtractCamelCaseInitials() {
        extractInitials("FirstLast", "FL");
    }

    @Test
    public void shouldExtractHyphenSeparatedNames() {
        extractInitials("First-Last", "FL");
    }

    @Test
    public void shouldExtractUnderScoreSeparatedNames() {
        extractInitials("First_Last", "FL");
    }

    @Test
    public void shouldExtractPlainInitials() {
        extractInitials("FL", "FL");
    }

    @Test
    public void shouldExtractNumeralSeparatedNames() {
        extractInitials("First2Last", "FL");
    }

    @Test
    public void shouldExtractFirstAndLastWithInitialsRun() {
        extractInitials("FLast", "FL");
    }

    @Test
    public void shouldExtractFirstAndLastWithTripleInitialsRun() {
        extractInitials("FMLast", "FL");
    }

    private void extractInitials(String name, String expectedInitials) {
        when(userId.getUserName()).thenReturn(name);
        String initials = UserIdInitialsExtractor.getInitials(userId);
        assertThat(initials, is(expectedInitials));
        System.out.println(name + " ---> " + initials);
    }

}

package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SaltedPasswordDigestReadConverterTestCase {

    private byte [] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16};

    private final String encodedString = "000102030405060708090a0b0d0e0f10";

    private SaltedPasswordDigestReadConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new SaltedPasswordDigestReadConverter();
    }

    @Test
    public void shouldConvertLowerCaseBase16StringToSaltedPasswordDigest() {
        SaltedPasswordDigest digest = converter.convert(encodedString.toLowerCase(Locale.ENGLISH));
        assertThat(digest.getBytes(), is(bytes));
    }

    @Test
    public void shouldConvertUpperCaseBase16StringToSaltedPasswordDigest() {
        SaltedPasswordDigest digest = converter.convert(encodedString.toUpperCase(Locale.ENGLISH));
        assertThat(digest.getBytes(), is(bytes));
    }
}

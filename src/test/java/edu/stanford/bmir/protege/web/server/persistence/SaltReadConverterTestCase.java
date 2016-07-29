package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.auth.Salt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SaltReadConverterTestCase {

    private byte [] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16};

    private final String encodedString = "000102030405060708090a0b0d0e0f10";

    private SaltReadConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new SaltReadConverter();
    }

    @Test
    public void shouldConvertBase16LowerCaseStringToSalt() {
        Salt salt = converter.convert(encodedString.toLowerCase());
        assertThat(salt.getBytes(), is(bytes));
    }

    @Test
    public void shouldConvertBase16UpperCaseStringToSalt() {
        Salt salt = converter.convert(encodedString.toUpperCase());
        assertThat(salt.getBytes(), is(bytes));
    }
}

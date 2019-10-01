package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.auth.Salt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SaltWriteConverterTestCase {

    private byte [] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16};

    private final String encodedString = "000102030405060708090a0b0d0e0f10";

    private SaltWriteConverter converter;

    @Mock
    private Salt salt;


    @Before
    public void setUp() {
        converter = new SaltWriteConverter();
        when(salt.getBytes()).thenReturn(bytes);
    }

    @Test
    public void shouldConvertSaltToBase() {
        String convertedSalt = converter.convert(salt);
        assertThat(convertedSalt, is(encodedString));
    }
}

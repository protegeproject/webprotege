package edu.stanford.bmir.protege.web.server.persistence;

import com.google.common.io.BaseEncoding;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SaltedPasswordDigestWriteConverterTestCase {

    private final byte [] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16};

    private final String encodedString = "000102030405060708090a0b0d0e0f10";

    private SaltedPasswordDigestWriteConverter converter;

    private SaltedPasswordDigest digest;

    @Before
    public void setUp() throws Exception {
        converter = new SaltedPasswordDigestWriteConverter();
        digest = new SaltedPasswordDigest(bytes);
    }


    @Test
    public void shouldConvertSaltedPasswordDigestToBase16LowerCaseString() {
        String convertedString = converter.convert(digest);
        assertThat(convertedString, is(encodedString));
    }

}

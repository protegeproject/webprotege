package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.io.BaseEncoding;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class PasswordDigestAlgorithm_ITestCase {


    public static final String CLEAR_TEXT_PASSWORD = "password";

    public static final String DIGESTED_PASSWORD = "bcc1ae0ca0834b41787c8c1ecd8feba8";

    public static final String SALT = "54d4bff73b7df8df";

    private PasswordDigestAlgorithm algorithm;

    @Before
    public void setUp() throws Exception {
        algorithm = new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider());
    }

    @Test
    public void shouldDigestPassword() {
        Salt salt = new Salt(BaseEncoding.base16().lowerCase().decode(SALT));
        SaltedPasswordDigest digest = algorithm.getDigestOfSaltedPassword(CLEAR_TEXT_PASSWORD, salt);
        assertThat(BaseEncoding.base16().lowerCase().encode(digest.getBytes()), is(DIGESTED_PASSWORD));
    }


}

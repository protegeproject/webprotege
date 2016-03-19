package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.io.BaseEncoding;
import edu.stanford.bmir.protege.web.server.user.UserRecord;
import edu.stanford.bmir.protege.web.shared.auth.Md5DigestAlgorithmProvider;
import edu.stanford.bmir.protege.web.shared.auth.PasswordDigestAlgorithm;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class DigestedPassword_TestCase {

    public static final String DIGESTED_PASSWORD = "bcc1ae0ca0834b41787c8c1ecd8feba8";

    public static final String SALT = "54d4bff73b7df8df";

    @Test
    public void shouldGenerateSameDigestedPassword() {
        PasswordDigestAlgorithm passwordDigestAlgorithm = new PasswordDigestAlgorithm(new Md5DigestAlgorithmProvider());
        Salt salt = new Salt(BaseEncoding.base16().lowerCase().decode(SALT));
        SaltedPasswordDigest digest = passwordDigestAlgorithm.getDigestOfSaltedPassword("password", salt);
        assertThat(digest.getBytes(), is(BaseEncoding.base16().lowerCase().decode(DIGESTED_PASSWORD)));
    }
}

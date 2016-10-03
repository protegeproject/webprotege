package edu.stanford.bmir.protege.web.server.persistence;

import com.google.common.io.BaseEncoding;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;

import org.springframework.data.convert.WritingConverter;

import java.util.Locale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
@WritingConverter
public class SaltedPasswordDigestWriteConverter implements Converter<SaltedPasswordDigest, String> {

    @Override
    public String convert(SaltedPasswordDigest saltedPasswordDigest) {
        return BaseEncoding.base16().encode(saltedPasswordDigest.getBytes()).toLowerCase(Locale.ENGLISH);
    }
}

package edu.stanford.bmir.protege.web.server.persistence;

import com.google.common.io.BaseEncoding;
import edu.stanford.bmir.protege.web.shared.auth.Salt;

import java.util.Locale;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 */
public class SaltReadConverter implements Converter<String, Salt> {

    @Override
    public Salt convert(String s) {
        BaseEncoding encoding = BaseEncoding.base16();
        byte [] bytes = encoding.decode(s.toUpperCase(Locale.ENGLISH));
        return new Salt(bytes);
    }
}

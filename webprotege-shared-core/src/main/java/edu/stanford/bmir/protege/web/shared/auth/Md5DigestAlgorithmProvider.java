package edu.stanford.bmir.protege.web.shared.auth;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class Md5DigestAlgorithmProvider implements Provider<MessageDigestAlgorithm> {
    @Override
    public MessageDigestAlgorithm get() {
        return new Md5MessageDigestAlgorithm();
    }
}

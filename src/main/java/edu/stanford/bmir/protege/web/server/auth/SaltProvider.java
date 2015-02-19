package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.Salt;

import javax.inject.Provider;
import java.util.Random;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/15
 */
public class SaltProvider implements Provider<Salt> {

    private Random random;

    public SaltProvider() {
        random = new Random();
    }

    @Override
    public Salt get() {
        byte [] bytes = new byte[8];
        random.nextBytes(bytes);
        return new Salt(bytes);
    }
}

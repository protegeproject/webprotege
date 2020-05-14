package edu.stanford.bmir.protege.web.client.uuid;


import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.client.uuid.UuidV4.uuidv4;

public class UuidV4Provider {

    @Inject
    public UuidV4Provider() {
    }

    /**
     * Generates a random UUID
     */
    @Nonnull
    public String get() {
        return uuidv4();
    }
}

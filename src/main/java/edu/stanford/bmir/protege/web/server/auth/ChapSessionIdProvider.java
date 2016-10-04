package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.ChapSessionId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChapSessionIdProvider implements Provider<ChapSessionId> {

    @Inject
    public ChapSessionIdProvider() {
    }

    @Override
    public ChapSessionId get() {
        return new ChapSessionId(getUuid());
    }

    private String getUuid() {
        return UUID.randomUUID().toString();
    }
}

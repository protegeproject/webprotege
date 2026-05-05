package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple in-memory cache for the OIDC discovery document.
 */
public class OidcDiscoveryCache {

    private final ObjectMapper objectMapper;

    private final ReentrantLock lock = new ReentrantLock();

    private volatile OidcDiscoveryDocument cached;

    @Inject
    public OidcDiscoveryCache(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Nonnull
    public OidcDiscoveryDocument get(@Nonnull String issuerUri) throws IOException {
        OidcDiscoveryDocument doc = cached;
        if (doc != null && !doc.isStale(OidcDiscoveryDocument.defaultMaxCacheAgeMillis())) {
            doc.assertIssuerCompatible(issuerUri);
            return doc;
        }
        lock.lock();
        try {
            doc = cached;
            if (doc != null && !doc.isStale(OidcDiscoveryDocument.defaultMaxCacheAgeMillis())) {
                doc.assertIssuerCompatible(issuerUri);
                return doc;
            }
            OidcDiscoveryDocument fresh = OidcDiscoveryDocument.fetch(issuerUri, objectMapper);
            fresh.assertIssuerCompatible(issuerUri);
            cached = fresh;
            return fresh;
        } finally {
            lock.unlock();
        }
    }

    @VisibleForTesting
    void clearForTests() {
        lock.lock();
        try {
            cached = null;
        } finally {
            lock.unlock();
        }
    }
}

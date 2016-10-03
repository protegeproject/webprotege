package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoClient;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class MongoClientProvider implements Provider<MongoClient> {

    @Nonnull
    private final String host;

    @Nonnull
    private final Integer port;

    @Inject
    public MongoClientProvider(@DbHost String dbHost, @DbPort Integer dbPort) {
        this.host = checkNotNull(dbHost);
        this.port = checkNotNull(dbPort);
    }

    @Override
    public MongoClient get() {
        System.out.println("Getting MONGO CLIENT");
        return new MongoClient(host, port);
    }
}

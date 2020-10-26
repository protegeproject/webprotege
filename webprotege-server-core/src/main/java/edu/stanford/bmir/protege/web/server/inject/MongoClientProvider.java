package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.*;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
@ApplicationSingleton
public class MongoClientProvider implements Provider<MongoClient> {

    private static final Logger logger = LoggerFactory.getLogger(MongoClientProvider.class);

    public static final int DEFAUL_PORT = 27017;

    public static final String DEFAULT_HOST = "localhost";

    @Nonnull
    private final Optional<String> host;

    @Nonnull
    private final Optional<Integer> port;

    @Nonnull
    private Optional<String> uri;

    @Nonnull
    private final Optional<MongoCredential> mongoCredential;

    @Nonnull
    private ApplicationDisposablesManager disposableObjectManager;

    @Inject
    public MongoClientProvider(@Nonnull @DbHost Optional<String> dbHost,
                               @Nonnull @DbPort Optional<Integer> dbPort,
                               @Nonnull @DbUri Optional<String> uri,
                               @Nonnull Optional<MongoCredential> mongoCredential,
                               @Nonnull ApplicationDisposablesManager disposableObjectManager) {
        this.host = checkNotNull(dbHost);
        this.port = checkNotNull(dbPort);
        this.uri = checkNotNull(uri);
        this.mongoCredential = checkNotNull(mongoCredential);
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
    }

    @Override
    public MongoClient get() {
        logger.info("Creating MongoClient database connection");
        var mongoClient = uri.map(u -> {
            logger.info("Creating MongoClient using Client URI");
            return new MongoClient(new MongoClientURI(u));
        }).orElseGet(this::getClientUsingHostAndPort);
        logger.info("Created MongoClient database connection");
        disposableObjectManager.register(() -> {
            logger.info("Closing MongoClient database connection...");
            mongoClient.close();
            logger.info("    ...closed MongoClient database connection");
        });
        return mongoClient;
    }

    private MongoClient getClientUsingHostAndPort() {
        var serverAddress = new ServerAddress(host.orElse(DEFAULT_HOST),
                                              port.orElse(DEFAUL_PORT));
        var seeds = Collections.singletonList(serverAddress);
        return mongoCredential.map(Collections::singletonList).map(credentials -> {
            logger.info("Creating MongoClient database connection with credentials for authentication");
            return new MongoClient(seeds, credentials);
        }).orElseGet(() -> {
            logger.info("Created MongoClient database connection without credentials for authentication");
            return new MongoClient(seeds);
        });
    }
}

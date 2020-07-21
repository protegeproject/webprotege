package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import edu.stanford.bmir.protege.web.server.app.ApplicationDisposablesManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
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

    @Nonnull
    private final String host;

    @Nonnull
    private final Integer port;

    @Nonnull
    private final Optional<MongoCredential> mongoCredential;

    @Nonnull
    private ApplicationDisposablesManager disposableObjectManager;

    @Inject
    public MongoClientProvider(@DbHost String dbHost,
                               @DbPort Integer dbPort,
                               @Nonnull Optional<MongoCredential> mongoCredential,
                               @Nonnull ApplicationDisposablesManager disposableObjectManager) {
        this.host = checkNotNull(dbHost);
        this.port = checkNotNull(dbPort);
        this.mongoCredential = checkNotNull(mongoCredential);
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
    }

    @Override
    public MongoClient get() {
        var serverAddress = new ServerAddress(host, port);
        var mongoClient = mongoCredential
                .map(Collections::singletonList)
                .map(credentials -> {
                    logger.info("Creating MongoClient database connection with credentials for authentication");
                    return new MongoClient(serverAddress, credentials);
                })
                .orElseGet(() -> {
                    logger.info("Created MongoClient database connection without credentials for authentication");
                    return new MongoClient(serverAddress);
                });
        logger.info("Created MongoClient database connection");
        disposableObjectManager.register(() -> {
            logger.info("Closing MongoClient database connection...");
            mongoClient.close();
            logger.info("    ...closed MongoClient database connection");
        });
        return mongoClient;
    }
}

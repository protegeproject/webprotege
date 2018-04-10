package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.app.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

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
    private DisposableObjectManager disposableObjectManager;

    @Inject
    public MongoClientProvider(@DbHost String dbHost,
                               @DbPort Integer dbPort,
                               @Nonnull DisposableObjectManager disposableObjectManager) {
        this.host = checkNotNull(dbHost);
        this.port = checkNotNull(dbPort);
        this.disposableObjectManager = disposableObjectManager;
    }

    @Override
    public MongoClient get() {
        MongoClient client = new MongoClient(host, port);
        logger.info("Created MongoClient database connection");
        disposableObjectManager.register(() -> {
            logger.info("Closing MongoClient database connection");
            client.close();
            logger.info("Closed MongoClient database connection");
        });
        return client;
    }
}

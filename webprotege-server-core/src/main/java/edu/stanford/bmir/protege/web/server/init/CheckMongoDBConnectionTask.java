package edu.stanford.bmir.protege.web.server.init;

import com.mongodb.MongoClient;
import com.mongodb.MongoTimeoutException;
import edu.stanford.bmir.protege.web.server.inject.DbHost;
import edu.stanford.bmir.protege.web.server.inject.DbPort;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckMongoDBConnectionTask implements ConfigurationTask {

    @Nonnull
    private final MongoClient mongoClient;

    private final String dbHost;

    private final int dbPort;

    @Inject
    public CheckMongoDBConnectionTask(@Nonnull MongoClient mongoClient,
                                      @DbHost String dbHost, @DbPort int dbPort) {
        this.mongoClient = checkNotNull(mongoClient);
        this.dbHost = checkNotNull(dbHost);
        this.dbPort = dbPort;
    }

    @Override
    public void run() throws WebProtegeConfigurationException {
        try {
            mongoClient.listDatabaseNames().first();
        } catch (MongoTimeoutException e) {
            throw new WebProtegeConfigurationException(getUnknownHostErrorMessage());
        }
    }

    private String getUnknownHostErrorMessage() {
        return String.format(
                "Could not connect to database on %s at port %d.  " +
                        "Please make sure the mongod daemon is running at this address.",
                dbHost,
                dbPort
        );
    }

}

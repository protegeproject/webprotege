package edu.stanford.bmir.protege.web.server.init;

import com.mongodb.MongoClient;
import com.mongodb.MongoTimeoutException;
import edu.stanford.bmir.protege.web.server.inject.DbHost;
import edu.stanford.bmir.protege.web.server.inject.DbPort;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckMongoDBConnectionTask implements ConfigurationTask {

    private final String dbHost;

    private final int dbPort;

    @Inject
    public CheckMongoDBConnectionTask(@DbHost String dbHost, @DbPort int dbPort) {
        this.dbHost = checkNotNull(dbHost);
        this.dbPort = dbPort;
    }

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        try {
            MongoClient mongoClient = new MongoClient(dbHost, dbPort);
            mongoClient.getDatabaseNames();
            mongoClient.close();
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

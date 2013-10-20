package edu.stanford.bmir.protege.web.server.db.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import java.net.UnknownHostException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/03/2013
 */
public class MongoDBManager {

    private static MongoDBManager instance;

    public static final String PORT_PROPERTY_NAME = "mongodb.port";

    public static final String HOST_PROPERTY_NAME = "mongodb.host";

    public static final String DEFAULT_HOST_NAME = "localhost";

    public static final int DEFAULT_PORT = 27017;




    private MongoClient client = null;

    private static final ServerAddress address;



    static {
        final String hostName = getHostName();
        final int port = getPort();

        try {
            address = new ServerAddress(hostName, port);
            WebProtegeLoggerManager.get(MongoDBManager.class).info("mongod address: " + address);
        }
        catch (IllegalArgumentException e) {
            throw new WebProtegeConfigurationException("Port or host name of database out of range: " + hostName + " port " + port);
        }
        catch (UnknownHostException e) {
            throw new WebProtegeConfigurationException(getUnknownHostErrorMessage(hostName, port));
        }

    }

    private static int getPort() {
        Optional<String> overridingPort = WebProtegeProperties.get().getDBPort();
        if (!overridingPort.isPresent()) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(overridingPort.get());
        }
        catch (NumberFormatException e) {
            System.err.println("Invalid port specification in mongod config file (port = " + overridingPort.get() + ").  Using default port.");
            return DEFAULT_PORT;
        }
    }

    private static String getHostName() {
        Optional<String> overridingHostName = WebProtegeProperties.get().getDBHost();
        if(overridingHostName.isPresent()) {
            return overridingHostName.get();
        }
        else {
            return DEFAULT_HOST_NAME;
        }
    }

    private static String getUnknownHostErrorMessage(String hostName, int port) {
        return "Could not connect to database on " + hostName + " port " + port + ".  Please make sure the mongod daemon is running at this address.";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private MongoDBManager() {
        try {
            client = new MongoClient(address);
            testConnection();
        }
        catch (Exception e) {
            // The call failed for some reason.  We cannot proceed without a properly initialized connection
            throw new WebProtegeConfigurationException(getMongoDBNotFoundMessage(), e);
        }
    }

    /**
     * Tests the database connection by asking for the names of the available databases.
     */
    private void testConnection() throws Exception {
        client.getDatabaseNames();
    }


    /**
     * Gets the MongoDBManager.
     * @return The one and only instance of MongoDBManager.  Not {@code null}.
     * @throws WebProtegeConfigurationException if a connection to mongodb could not be established.
     */
    public static synchronized MongoDBManager get() throws WebProtegeConfigurationException {
        if (instance == null) {
            instance = new MongoDBManager();
        }
        return instance;
    }


    private MongoClient getClient() {
        return client;
    }


    public void dispose() {
        client.close();
    }

    private String getMongoDBNotFoundMessage() {
        return "WebProtege could not connect to mongodb.  Please check that the mongod daemon is either running on the default host (" + DEFAULT_HOST_NAME + ") and default port (" + DEFAULT_PORT + "), or that the mongod daemon is running on a custom host and port and that you have specified the custom host and port in the protege.properties with the parameters " + HOST_PROPERTY_NAME + " and " + PORT_PROPERTY_NAME;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DB getProjectDB(ProjectId projectId) {
        return client.getDB("prj-" + projectId.getId());
    }


}

package edu.stanford.bmir.protege.web.server.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import edu.stanford.bmir.protege.web.server.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.WebProtegeFileStore;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.smi.protege.util.ApplicationProperties;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

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
        String overridingPortString = ApplicationProperties.getString(PORT_PROPERTY_NAME);
        if (overridingPortString == null) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(overridingPortString);
        }
        catch (NumberFormatException e) {
            System.err.println("Invalid port specification in mongod config file (port = " + overridingPortString + ").  Using default port.");
            return DEFAULT_PORT;
        }
    }

    private static String getHostName() {
        String overridingHostName = ApplicationProperties.getString(HOST_PROPERTY_NAME);
        if(overridingHostName != null) {
            return overridingHostName;
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

    public MongoDBManager() {
        try {
            client = new MongoClient(address);
            testConnection();
        }
        catch (Exception e) {
            // The call failed for some reason.  We cannot proceed without a properly initialized connection
            WebProtegeWebAppFilter.setConfigError(new WebProtegeConfigurationException(getMongoDBNotFoundMessage(), e));
        }
    }

    /**
     * Tests the database connection by asking for the names of the available databases.
     */
    private void testConnection() throws Exception {
        List<String> databaseNames = client.getDatabaseNames();
    }



    public static synchronized MongoDBManager get() {
        if (instance == null) {
            instance = new MongoDBManager();
        }
        return instance;
    }


    public MongoClient getClient() {
        return client;
    }


    public void dispose() {
        client.close();
    }

    private String getMongoDBNotFoundMessage() {
        return "WebProtege could not connect to mongodb.  Please check that the mongod daemon is either running on the default host (" + DEFAULT_HOST_NAME + ") and default port (" + DEFAULT_PORT + "), or that the mongod daemon is running on a custom host and port and that you have specified the custom host and port in the protege.properties with the parameters " + HOST_PROPERTY_NAME + " and " + PORT_PROPERTY_NAME;
    }

}

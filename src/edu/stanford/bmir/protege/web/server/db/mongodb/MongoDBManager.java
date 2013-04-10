package edu.stanford.bmir.protege.web.server.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import edu.stanford.bmir.protege.web.server.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.WebProtegeFileStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/03/2013
 */
public class MongoDBManager {

    private static MongoDBManager instance;

    public static final String MONGOD_CONF_FILE_NAME = "mongod.conf";

    public static final String PORT_PROPERTY_NAME = "port";


    public static final String DEFAULT_HOST_NAME = "localhost";

    public static final int DEFAULT_PORT = 27017;




    private MongoClient client = null;

    private static final ServerAddress address;



    static {
        final String hostName = DEFAULT_HOST_NAME;
        Properties properties = getMongodConfig();
        final int port = getPort(properties);

        try {
            address = new ServerAddress(hostName, port);
            System.out.println("mongod address: " + address);
        }
        catch (IllegalArgumentException e) {
            throw new WebProtegeConfigurationException("Port or host name of database out of range: " + hostName + " port " + port);
        }
        catch (UnknownHostException e) {
            throw new WebProtegeConfigurationException(getUnknownHostErrorMessage(hostName, port));
        }

    }

    private static int getPort(Properties mongodConfigProperties) {
        String overridingPortString = mongodConfigProperties.getProperty(PORT_PROPERTY_NAME);
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

    private static Properties getMongodConfig() {
        File mongodConfFile = getMongodConfigFile();
        Properties properties = new Properties();
        if (!mongodConfFile.exists()) {
            System.err.println("WARNING: mongod config file does not exist.  Expected to find it at " + mongodConfFile.getAbsolutePath());
            return properties;
        }
        try {
            final BufferedInputStream is = new BufferedInputStream(new FileInputStream(mongodConfFile));
            properties.load(is);
            is.close();
        }
        catch (IOException e) {
            System.err.println("There was a problem reading the mongod properties file: " + e.getMessage());
        }
        return properties;
    }

    private static File getMongodConfigFile() {
        WebProtegeFileStore fileStore = WebProtegeFileStore.getInstance();
        File webProtegeDataDirectory = fileStore.getDataDirectory();
        return new File(webProtegeDataDirectory, MONGOD_CONF_FILE_NAME);
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
        catch (MongoException e) {
            // The call failed for some reason.  We cannot proceed without a properly initialized connection
            throw new WebProtegeConfigurationException("There was a problem establishing a connection to the webprotege database at " + address + ".  Please check that the address and port are correct and that the database daemon is running at the specified address on the specified port.");
        }
    }

    /**
     * Tests the database connection by asking for the names of the available databases.
     */
    private void testConnection() {
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

    public static void main(String[] args) {
        UUID uuid = UUID.fromString("matthewhorridge");
        System.out.println(uuid.toString());
    }
}

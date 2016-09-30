package edu.stanford.bmir.protege.web.server.app;

import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.permissions.PermissionReadConverter;
import edu.stanford.bmir.protege.web.server.permissions.PermissionWriteConverter;
import edu.stanford.bmir.protege.web.server.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 */
@Configuration
@EnableMongoRepositories(basePackages = {"edu.stanford.bmir.protege.web.server","edu.stanford.bmir.protege.web.shared"})
@PropertySource(value="classpath:webprotege.properties")
public class WebProtegeApplicationConfig extends AbstractMongoConfiguration {

    public static final String MONGO_PORT_PROPERTY_NAME = "mongodb.port";

    public static final String MONGO_HOST_PROPERTY_NAME = "mongodb.host";


    private static final String DATABASE_NAME = "webprotege";

    private static final int DEFAULT_PORT = 27017;

    private static final String DEFAULT_HOST = "localhost";


    /**
     * Environment provides properties.  Since we declare a property source (above) that is webprotege.properties
     * this is loaded and accessible via the Environment.  Note that, if a property is set on the command line (via -D)
     * this value will override the value in webprotege.properties.
     */
    @Autowired
    Environment environment;

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public Mongo mongo() throws Exception {
        String host = getHost();
        int port = getPort();
        try {
            ServerAddress address = new ServerAddress(host, port);
            Mongo mongo = new Mongo(address);
            mongo.setWriteConcern(WriteConcern.SAFE);
            return mongo;
        }
        catch (IllegalArgumentException e) {
            throw new WebProtegeConfigurationException("Port or host name of database out of range: " + host + " port " + port);
        }
        catch (UnknownHostException e) {
            throw new WebProtegeConfigurationException(getUnknownHostErrorMessage(host, port));
        }
    }

    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        // We provide custom conversions for some value objects so that the resulting serialized json looks nicer,
        // is more readable, and is smaller.
        MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter();
        List<Converter<?,?>> converters = new ArrayList<Converter<?,?>>();
        converters.add(new ProjectIdReadConverter());
        converters.add(new ProjectIdWriteConverter());
        converters.add(new UserIdReadConverter());
        converters.add(new UserIdWriteConverter());
        converters.add(new EmailAddressReadConverter());
        converters.add(new EmailAddressWriteConverter());
        converters.add(new SaltReadConverter());
        converters.add(new SaltWriteConverter());
        converters.add(new SaltedPasswordDigestReadConverter());
        converters.add(new SaltedPasswordDigestWriteConverter());
        converters.add(new PermissionReadConverter());
        converters.add(new PermissionWriteConverter());
        converters.add(new PermissionSetReadConverter());
        converters.add(new OWLEntityReadConverter());
        converters.add(new OWLEntityWriteConverter());
        converters.add(new MilestoneReadConverter());
        converters.add(new MilestoneWriteConverter());
//        converters.add(new MentionReadConverter());
//        converters.add(new IssueEventReadConverter());

        mappingMongoConverter.setCustomConversions(new CustomConversions(converters));
        return mappingMongoConverter;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int getPort() {
        String port = environment.getProperty(MONGO_PORT_PROPERTY_NAME);
        if(port == null) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(port);
        }
        catch (NumberFormatException e) {
            System.err.println("Invalid port specification in mongod config file (port = " + port + ").  Using default port.");
            return DEFAULT_PORT;
        }
    }

    private String getHost() {
        String host = environment.getProperty(MONGO_HOST_PROPERTY_NAME);
        if(host == null) {
            return DEFAULT_HOST;
        }
        return host;
    }

    private static String getUnknownHostErrorMessage(String hostName, int port) {
        return "Could not connect to database on " + hostName + " port " + port + ".  Please make sure the mongod daemon is running at this address.";
    }
}

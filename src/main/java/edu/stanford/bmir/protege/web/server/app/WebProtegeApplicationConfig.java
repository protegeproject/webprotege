package edu.stanford.bmir.protege.web.server.app;

import com.google.common.base.Optional;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.persistence.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
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
@EnableMongoRepositories(basePackages = "edu.stanford.bmir.protege.web.server")
public class WebProtegeApplicationConfig extends AbstractMongoConfiguration {


    public static final String DEFAULT_HOST_NAME = "localhost";

    public static final int DEFAULT_PORT = 27017;

    @Override
    protected String getDatabaseName() {
        return "webprotege";
    }

    @Override
    public Mongo mongo() throws Exception {
        Mongo mongo = new Mongo(address);
        mongo.setWriteConcern(WriteConcern.SAFE);
        return mongo;
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

        mappingMongoConverter.setCustomConversions(new CustomConversions(converters));
        return mappingMongoConverter;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final ServerAddress address;



    static {
        final String hostName = getHostName();
        final int port = getPort();

        try {
            address = new ServerAddress(hostName, port);
            WebProtegeLoggerManager.get(WebProtegeApplicationConfig.class).info("mongod address: " + address);
        }
        catch (IllegalArgumentException e) {
            throw new WebProtegeConfigurationException("Port or host name of database out of range: " + hostName + " port " + port);
        }
        catch (UnknownHostException e) {
            throw new WebProtegeConfigurationException(getUnknownHostErrorMessage(hostName, port));
        }

    }

    private static int getPort() {
        Optional<String> overridingPortString = WebProtegeProperties.get().getDBPort();
        if (!overridingPortString.isPresent()) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(overridingPortString.get());
        }
        catch (NumberFormatException e) {
            System.err.println("Invalid port specification in mongod config file (port = " + overridingPortString + ").  Using default port.");
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
}

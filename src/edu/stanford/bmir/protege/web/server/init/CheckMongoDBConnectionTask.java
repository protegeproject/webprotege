package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;

import javax.servlet.ServletContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckMongoDBConnectionTask implements ConfigurationTask {

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
            MongoDBManager.get();
    }
}

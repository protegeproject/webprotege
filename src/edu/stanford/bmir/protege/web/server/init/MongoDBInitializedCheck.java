package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class MongoDBInitializedCheck implements ConfigurationCheck {

    @Override
    public void runCheck() throws WebProtegeConfigurationException {
            MongoDBManager.get();
    }
}

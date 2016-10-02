package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoClient;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class MongoClientProvider implements Provider<MongoClient> {

    @Override
    public MongoClient get() {
        // TODO: Host and Port
        return new MongoClient();
    }
}

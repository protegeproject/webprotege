package edu.stanford.bmir.protege.web.server.inject;

import javax.inject.Provider;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class MongoDatabaseProvider implements Provider<MongoDatabase> {

    public static final String DATABASE_NAME = "webprotege";

    private final MongoClient mongoClient;

    @Inject
    public MongoDatabaseProvider(@Nonnull MongoClient mongoClient) {
        this.mongoClient = checkNotNull(mongoClient);
    }

    @Override
    public MongoDatabase get() {
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}

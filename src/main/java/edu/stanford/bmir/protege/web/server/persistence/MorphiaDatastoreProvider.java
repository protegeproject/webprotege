package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MorphiaDatastoreProvider implements Provider<Datastore> {

    @Nonnull
    private final Morphia morphia;

    @Nonnull
    private final MongoClient mongoClient;

    @Nonnull
    private final String dbName;

    @Inject
    public MorphiaDatastoreProvider(@Nonnull Morphia morphia,
                                    @Nonnull MongoClient mongoClient,
                                    @Nonnull @DbName String dbName) {
        this.morphia = checkNotNull(morphia);
        this.mongoClient = checkNotNull(mongoClient);
        this.dbName = checkNotNull(dbName);
    }

    @Override
    public Datastore get() {
        return morphia.createDatastore(mongoClient, dbName);
    }
}

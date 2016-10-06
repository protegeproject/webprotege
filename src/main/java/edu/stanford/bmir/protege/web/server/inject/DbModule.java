package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.persistence.DbName;
import edu.stanford.bmir.protege.web.server.persistence.MorphiaDatastoreProvider;
import edu.stanford.bmir.protege.web.server.persistence.MorphiaProvider;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@Module
public class DbModule {

    @Provides
    @DbHost
    public String provideDbHost(DbHostProvider dbHostProvider) {
        return dbHostProvider.get();
    }

    @Provides
    @DbPort
    public int provideDbPort(DbPortProvider dbPortProvider) {
        return dbPortProvider.get();
    }

    @Provides
    @Singleton
    public MongoClient provideMongoClient(MongoClientProvider provider) {
        return provider.get();
    }

    @Provides
    @Singleton
    public MongoDatabase provideMongoDatabase(MongoDatabaseProvider provider) {
        return provider.get();
    }

    @Provides
    public Morphia providesMorphia(MorphiaProvider provider) {
        return provider.get();
    }

    @Provides
    public Datastore provideDatastore(MorphiaDatastoreProvider provider) {
        return provider.get();
    }

    @Provides
    @DbName
    public String provideDbName() {
        return "webprotege";
    }
}

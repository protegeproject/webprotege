package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.persistence.DbName;
import edu.stanford.bmir.protege.web.server.persistence.MorphiaDatastoreProvider;
import edu.stanford.bmir.protege.web.server.persistence.MorphiaProvider;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Optional;

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
    @DbUsername
    public String provideDbUserName(WebProtegeProperties webProtegeProperties) {
        return webProtegeProperties.getDBUserName().orElse("");
    }

    @Provides
    @DbPassword
    char [] provideDbPassword(WebProtegeProperties webProtegeProperties) {
        return webProtegeProperties.getDBPassword().orElse("").toCharArray();
    }

    @Provides
    @DbAuthenticationSource
    String provideDbAuthenticationSource(WebProtegeProperties webProtegeProperties) {
        return webProtegeProperties.getDBAuthenticationSource().orElse("");
    }

    @Provides
    public Optional<MongoCredential> provideMongoCredentials(MongoCredentialProvider credentialsProvider) {
        return credentialsProvider.get();
    }

    @Provides
    @ApplicationSingleton
    public MongoClient provideMongoClient(MongoClientProvider provider) {
        return provider.get();
    }

    @Provides
    @ApplicationSingleton
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

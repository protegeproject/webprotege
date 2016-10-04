package edu.stanford.bmir.protege.web.server.inject;

import com.mongodb.client.MongoDatabase;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsConverter;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecordRepository;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecordRepositoryProvider;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 *
 * A module for binding Spring Data Repositories.
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public ProjectEntityCrudKitSettingsRepository provideProjectEntityCrudKitSettingsRepository(
            MongoDatabase database, ProjectEntityCrudKitSettingsConverter converter) {
        return new ProjectEntityCrudKitSettingsRepository(database, converter);
    }

    @Provides
    @Singleton
    public ProjectPermissionRecordRepository provideProjectPermissionRecordRepository(ProjectPermissionRecordRepositoryProvider provider) {
        return provider.get();
    }
}

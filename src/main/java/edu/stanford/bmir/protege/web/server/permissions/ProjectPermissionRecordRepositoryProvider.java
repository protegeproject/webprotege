package edu.stanford.bmir.protege.web.server.permissions;

import com.mongodb.client.MongoDatabase;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Oct 2016
 */
public class ProjectPermissionRecordRepositoryProvider implements Provider<ProjectPermissionRecordRepository> {

    @Nonnull
    private final MongoDatabase database;

    @Nonnull
    private final ProjectPermissionRecordConverter converter;

    @Inject
    public ProjectPermissionRecordRepositoryProvider(@Nonnull MongoDatabase database,
                                                     @Nonnull ProjectPermissionRecordConverter converter) {
        this.database = checkNotNull(database);
        this.converter = checkNotNull(converter);
    }

    @Override
    public ProjectPermissionRecordRepository get() {
        ProjectPermissionRecordRepository repository = new ProjectPermissionRecordRepository(
                database,
                converter);
        repository.ensureIndexes();
        return repository;
    }
}

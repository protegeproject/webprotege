package edu.stanford.bmir.protege.web.server.admin;

import edu.stanford.bmir.protege.web.server.inject.Application;
import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.server.app.DefaultApplicationSettings;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import org.mongodb.morphia.Datastore;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@Application
public class ApplicationSettingsManager implements Repository {

    private final Datastore datastore;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public ApplicationSettingsManager(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(ApplicationSettingsManager.class);
    }

    @Nonnull
    public ApplicationSettings getApplicationSettings() {
        readLock.lock();
        try {
            ApplicationSettings applicationSettings = datastore.get(ApplicationSettings.class, ApplicationSettings.ID);
            if (applicationSettings == null) {
                applicationSettings = DefaultApplicationSettings.get();
                datastore.save(applicationSettings);
            }
            return applicationSettings;
        } finally {
            readLock.unlock();
        }

    }

    public void setApplicationSettings(@Nonnull ApplicationSettings applicationSettings) {
        writeLock.lock();
        try {
            datastore.save(checkNotNull(applicationSettings));

        } finally {
            writeLock.unlock();
        }
    }
}

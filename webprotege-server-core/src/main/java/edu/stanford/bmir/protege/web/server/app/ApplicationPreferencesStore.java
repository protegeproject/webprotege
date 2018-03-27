package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.mongodb.morphia.Datastore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
@ApplicationSingleton
public class ApplicationPreferencesStore implements Repository {

    private final Datastore datastore;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Nullable
    private ApplicationPreferences cachedPreferences = null;

    @Inject
    public ApplicationPreferencesStore(@Nonnull Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    @Override
    public void ensureIndexes() {
        datastore.ensureIndexes(ApplicationPreferencesStore.class);
    }

    @Nonnull
    public ApplicationPreferences getApplicationPreferences() {
        if(cachedPreferences != null) {
            return cachedPreferences;
        }
        readLock.lock();
        try {
            ApplicationPreferences applicationPreferences = datastore.get(ApplicationPreferences.class, ApplicationPreferences.ID);
            if (applicationPreferences == null) {
                applicationPreferences = DefaultApplicationPreferences.get();
                datastore.save(applicationPreferences);
            }
            cachedPreferences = applicationPreferences;
            return applicationPreferences;
        } finally {
            readLock.unlock();
        }

    }

    public void setApplicationPreferences(@Nonnull ApplicationPreferences preferences) {
        writeLock.lock();
        try {
            cachedPreferences = preferences;
            datastore.save(checkNotNull(preferences));

        } finally {
            writeLock.unlock();
        }
    }
}

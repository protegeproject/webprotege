package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchIndex {

    private final Multimap<UserId, Watch<?>> userId2Watch = HashMultimap.create();

    private final Multimap<Watch<?>, UserId> watch2UserId = HashMultimap.create();

    private final Multimap<Object, Watch<?>> watchedObject2Watch = HashMultimap.create();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    public boolean addWatch(Watch<?> watch, UserId userId) {
        try {
            writeLock.lock();
            boolean added = userId2Watch.put(checkNotNull(userId), checkNotNull(watch));
            watch2UserId.put(watch, userId);
            watchedObject2Watch.put(watch.getWatchedObject(), watch);
            return added;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean removeWatch(Watch<?> watch, UserId userId) {
        try {
            writeLock.lock();
            boolean removed = userId2Watch.remove(checkNotNull(userId), checkNotNull(watch));
            watch2UserId.remove(watch, userId);
            watchedObject2Watch.remove(watch.getWatchedObject(), watch);
            return removed;
        } finally {
            writeLock.unlock();
        }
    }

    public Set<Watch<?>> getWatchesForUser(UserId userId) {
        try {
            readLock.lock();
            return new HashSet<>(userId2Watch.get(userId));
        } finally {
            readLock.unlock();
        }
    }

    public Set<Watch<?>> getWatchesOnEntity(OWLEntity entity) {
        try {
            readLock.lock();
            return new HashSet<>(watchedObject2Watch.get(entity));
        } finally {
            readLock.unlock();
        }
    }


    public Set<UserId> getUsersForWatch(Watch<?> watch) {
        try {
            readLock.lock();
            return new HashSet<>(watch2UserId.get(watch));
        } finally {
            readLock.unlock();
        }
    }
}

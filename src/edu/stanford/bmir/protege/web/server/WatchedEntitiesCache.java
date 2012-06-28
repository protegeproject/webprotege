package edu.stanford.bmir.protege.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import edu.stanford.bmir.protege.web.client.rpc.data.Watch;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.OntologyComponentFactory;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.Ontology_Component;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.User;
import edu.stanford.smi.protege.model.Project;

public class WatchedEntitiesCache {

    //We may need to use a different synchronization mechanism..
    private static Map<Project, WatchedEntitiesCache> projectsToCaches= new ConcurrentHashMap<Project, WatchedEntitiesCache>();

    //TODO: not clear that we need to store the maps and the inverse maps...
    private Map<String, Set<String>> usersToEntities = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> entitiesToUsers = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> usersToBranches = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> branchesToUsers = new HashMap<String, Set<String>>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private WatchedEntitiesCache(final Project project, OntologyComponentFactory factory) {
        projectsToCaches.put(project, this);

        if (factory == null) { return; }

        final Collection<User> allUserObjects = factory.getAllUserObjects();
        for (User user : allUserObjects) {
            if (user.getWatchedEntity() != null && !user.getWatchedEntity().isEmpty()) {
                final Collection<Ontology_Component> entities = user.getWatchedEntity();
                for (Ontology_Component entity : entities) {
                    String currentName = entity.getCurrentName(); //TODO: we have to watch out for name changes
                    if (currentName != null) {
                        addToMap(entitiesToUsers, currentName, user.getName());
                        addToMap(usersToEntities, user.getName(), currentName);
                    }
                }
            }
            if (user.getWatchedBranch() != null && !user.getWatchedBranch().isEmpty()) {
                final Collection<Ontology_Component> branches = user.getWatchedBranch();
                for (Ontology_Component branch : branches) {
                    String currentName = branch.getCurrentName(); //TODO: we have to watch out for name changes
                    if (currentName != null) {
                        addToMap(branchesToUsers, currentName, user.getName());
                        addToMap(usersToBranches, user.getName(), currentName);
                    }
                }
            }
        }
    }

    public static void init(final Project project, final OntologyComponentFactory factory){
        projectsToCaches.put(project, new WatchedEntitiesCache(project, factory));
    }

    public static WatchedEntitiesCache getCache(Project project){
        return projectsToCaches.get(project);
    }

    /**
     * Call returns a string, because the calling class (the NotificationSchedulerServlet) has only class names.
     *
     * To have it reconstitute Strings would further reduce performance for the caller.
     * @return
     */
    public Map<String, List<String>> getWatchedBranches() {
        try {
            lock.readLock().lock();
            final HashMap<String, List<String>> collector = new HashMap<String, List<String>>();
            for (Map.Entry<String, Set<String>> entry : branchesToUsers.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()){
                    collector.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
                }
            }
            return collector;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<String> getEntityWatches(String component) {
        try {
            lock.readLock().lock();
            if (entitiesToUsers.get(component) == null){
                return new HashSet<String>();
            }
            return new HashSet<String>(entitiesToUsers.get(component));
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<String> getBranchWatches(String component) {
        try {
            lock.readLock().lock();
            if (branchesToUsers.get(component) == null){
                return new HashSet<String>();
            }
            return new HashSet<String>(branchesToUsers.get(component));
        } finally {
            lock.readLock().unlock();
        }
    }

    public void addEntityWatch(String String, String user) {
        try {
            lock.writeLock().lock();
            addToMap(entitiesToUsers, String, user);
            addToMap(usersToEntities, user, String);
        } finally {
            lock.writeLock().unlock();
        }
    }


    public void addBranchWatch(String branchToWatch, String user) {
        try {
            lock.writeLock().lock();
            addToMap(branchesToUsers, branchToWatch, user);
            addToMap(usersToBranches, user, branchToWatch);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeEntityWatch( String entity, String user) {
        try {
            lock.writeLock().lock();
            removeFromMap(entitiesToUsers, entity, user);
            removeFromMap(usersToEntities, user, entity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeBranchWatch(String String, String user) {
        try {
            lock.writeLock().lock();
            removeFromMap(branchesToUsers, String, user);
            removeFromMap(usersToBranches, user, String);
        } finally {
            lock.writeLock().unlock();
        }
    }

    static void purgeCache() {
        projectsToCaches.clear();
    }

    public boolean isWatchedEntity(String user, String component){
        try {
            lock.readLock().lock();
            return mapContains(usersToEntities, user, component);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isWatchedBranch(String user, String entity){
        try {
            lock.readLock().lock();
            return mapContains(usersToBranches, user, entity);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Watch getWatchType(String user, String entity) {
        try {
            lock.readLock().lock();
            if (user == null) {
                return null;
            }

            boolean isWatchedEntity = isWatchedEntity(user, entity);
            boolean isWatchedBranch = isWatchedBranch(user, entity);

            if (isWatchedEntity && isWatchedBranch) {
                return Watch.BOTH;
            }
            if (isWatchedEntity) {
                return Watch.ENTITY_WATCH;
            }
            if (isWatchedBranch) {
                return Watch.BRANCH_WATCH;
            }
            return null; //no need to return Watch.UNWATCHED;
        } finally {
            lock.readLock().unlock();
        }
    }


    public <M extends Map<K, Set<V>>, K, V> void addToMap(M map, K key, V value) {
        Set<V> set = map.get(key);
        if (set == null) {
            set = new HashSet<V>();
            map.put(key, set);
        }
        set.add(value);
    }

    public <M extends Map<K, Set<V>>, K, V> boolean removeFromMap(M map, K key, V value) {
        Set<V> set = map.get(key);
        return set != null && set.remove(value);
    }

    public <M extends Map<K, Set<V>>, K, V> boolean mapContains(M map, K key, V value) {
        Set<V> set = map.get(key);
        return set != null && set.contains(value);
    }

}
package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.collect.MapMaker;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.util.Log;
import org.semanticweb.owlapi.io.OWLParserException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class OWLAPIProjectCache {

    private static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(OWLAPIProjectCache.class);

    private final ConcurrentMap<ProjectId, ProjectId> projectIdInterningMap;


    private final ReadWriteLock projectMapReadWriteLoc = new ReentrantReadWriteLock();

    private final Lock READ_LOCK = projectMapReadWriteLoc.readLock();

    private final Lock WRITE_LOCK = projectMapReadWriteLoc.writeLock();

    private Map<ProjectId, OWLAPIProject> projectId2ProjectMap = new ConcurrentHashMap<ProjectId, OWLAPIProject>();


    private ReadWriteLock LAST_ACCESS_LOCK = new ReentrantReadWriteLock();

    private Map<ProjectId, Long> lastAccessMap = new HashMap<ProjectId, Long>();



    /**
     * The period between purge checks (in ms).  Every 30 seconds.
     */
    public static final int PURGE_CHECK_PERIOD_MS = 30 * 1000;

    /**
     * Ellapsed time from the last access after which a project should be considered dormant (and should therefore
     * be purged).  This can interact with the frequency with which clients poll the project event queue (which is
     * be default every 10 seconds).
     */
    private static final long DORMANT_PROJECT_TIME_MS = 1 * 60 * 1000;


    public OWLAPIProjectCache() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                purgeDormantProjects();
            }
        }, 0, PURGE_CHECK_PERIOD_MS);


        MapMaker mapMaker = new MapMaker();
        projectIdInterningMap = mapMaker.concurrencyLevel(5).initialCapacity(20).weakKeys().makeMap();

    }


    /**
     * Gets the list of cached project ids.
     * @return A list of cached project ids.
     */
    private List<ProjectId> getCachedProjectIds() {
        try {

            READ_LOCK.lock();
            return new ArrayList<ProjectId>(lastAccessMap.keySet());
        }
        finally {
            READ_LOCK.unlock();
        }
    }


    private void purgeDormantProjects() {
        // No locking needed
        for (ProjectId projectId : getCachedProjectIds()) {
            long time = getLastAccessTime(projectId);
            long lastAccessTimeDiff = System.currentTimeMillis() - time;
            if (time == 0 || lastAccessTimeDiff > DORMANT_PROJECT_TIME_MS) {
                purge(projectId);
            }
        }
    }


    public OWLAPIProject getProject(ProjectId projectId) throws ProjectDocumentNotFoundException {
        synchronized (getInternedProjectId(projectId)) {
            try {
                OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
                OWLAPIProject project = projectId2ProjectMap.get(projectId);
                if (project == null) {
                    project = OWLAPIProject.getProject(documentStore);
                    projectId2ProjectMap.put(projectId, project);
                }
                logProjectAccess(projectId);
                return project;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            catch (OWLParserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets an interned {@link ProjectId} that is equal to the specified {@link ProjectId}
     * @param projectId The project id to intern.
     * @return The interned project Id.  Not {@code null}.
     */
    private ProjectId getInternedProjectId(ProjectId projectId) {
        projectIdInterningMap.putIfAbsent(projectId, projectId);
        return projectIdInterningMap.get(projectId);
    }

    public OWLAPIProject getProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException {
        try {
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.createNewProject(newProjectSettings);
            return getProject(documentStore.getProjectId());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void purge(ProjectId projectId) {
        try {
            WRITE_LOCK.lock();
            LAST_ACCESS_LOCK.writeLock().lock();
            OWLAPIProject project = projectId2ProjectMap.remove(projectId);
            lastAccessMap.remove(projectId);
            project.dispose();
            LOGGER.info(lastAccessMap.size() + " projects are being accessed");
        }
        finally {
            LAST_ACCESS_LOCK.writeLock().unlock();
            WRITE_LOCK.unlock();
            LOGGER.info("Purged project: " + projectId);
        }
    }

    /**
     * Gets the time of last cache access for a given project.
     * @param projectId The project id.
     * @return The time stamp of the last access of the specified project from the cache.  This time stamp will be 0
     *         if the project does not exist.
     */
    public long getLastAccessTime(ProjectId projectId) {
        Long timestamp = null;
        try {
            LAST_ACCESS_LOCK.readLock().lock();
            timestamp = lastAccessMap.get(projectId);
        }
        finally {
            LAST_ACCESS_LOCK.readLock().unlock();
        }
        if (timestamp == null) {
            return 0;
        }
        else {
            return timestamp;
        }
    }

    protected void logProjectAccess(final ProjectId projectId) {
        try {
            LAST_ACCESS_LOCK.writeLock().lock();
            long currentTime = System.currentTimeMillis();
            int currentSize = lastAccessMap.size();
            lastAccessMap.put(projectId, currentTime);
            if(lastAccessMap.size() > currentSize) {
                LOGGER.info(lastAccessMap.size() + " projects are being accessed");
            }
        }
        finally {
            LAST_ACCESS_LOCK.writeLock().unlock();
        }
    }
}
